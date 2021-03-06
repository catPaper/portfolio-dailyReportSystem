package actions;

import java.io.IOException;
import java.sql.Time;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReactionService;
import services.ReportService;
import services.UserTmpService;
import utils.CalculationUtil;

/**
 * トップページに関する処理を行うActionクラス
 * @author ryouta.osada
 *
 */
public class TopAction extends ActionBase {

    private ReportService reportService;
    private UserTmpService tmpService;
    private ReactionService reactionService;

    /**
     * invokeメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        reportService = new ReportService();
        tmpService = new UserTmpService();
        reactionService = new ReactionService();

        //メソッドを実行
        invoke();

        reportService.close();
        tmpService.close();
        reactionService.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException,IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);

        //ログインした従業員の一時データがデータベースに無ければ作成
        if(tmpService.countAllMine(loginEmployee) == 0) {
            tmpService.create(loginEmployee);
        }
        //一時データの出勤時刻を取得
        Time punchIn = tmpService.getPuncIn(loginEmployee);

        //未読メッセージのみを表示するかどうかのパラメータ値を受け取り
        //nullまたは未読メッセージがない場合はデフォルトで全件表示するよう設定する
        Integer exist_unread = (reportService.countAllMineUnRead(loginEmployee) > 0)
                                ? AttributeConst.EXIST_FLAG_TRUE.getIntegerValue()
                                : AttributeConst.EXIST_FLAG_FALSE.getIntegerValue();
        Integer isShow;
        if(getRequestParam(AttributeConst.REP_SHOW_UNREAD) == null
                || exist_unread == AttributeConst.EXIST_FLAG_FALSE.getIntegerValue()) {
            isShow = AttributeConst.SHOW_FLAG_FALSE.getIntegerValue();
        }else {
            isShow = toNumber(getRequestParam(AttributeConst.REP_SHOW_UNREAD));
        }


        //ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得
        int page = getPage();
        List<ReportView> reports;
        //ログイン中の従業員が作成した日報データの件数を取得
        long myReportCount;
        if(isShow == AttributeConst.SHOW_FLAG_TRUE.getIntegerValue()) {
            //ログイン中の従業員の未読コメントのついた日報のみを表示する
            reports = reportService.getMineUnReadPerPage(loginEmployee, page);
            myReportCount = reportService.countAllMineUnRead(loginEmployee);
        }else {
            //ログイン中の従業員の日報を表示する
            reports = reportService.getMinePerPage(loginEmployee,page);
            myReportCount = reportService.countAllMine(loginEmployee);
        }

        //リアクション数のリストを作成する
        List<Long> reactions = reactionService.getAllCountReactToReport(reports);

        putRequestScope(AttributeConst.REPORTS,reports);                    //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT,myReportCount);            //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE,page);                          //ページ数
        putRequestScope(AttributeConst.MAX_ROW,JpaConst.ROW_PER_PAGE);      //1ページに表示するレコードの数
        putRequestScope(AttributeConst.TMP_PUNCH_IN,punchIn);               //ログイン中の従業員の出勤時刻情報
        putRequestScope(AttributeConst.TOKEN,getTokenId());                 //CSRF対策用トークン
        putRequestScope(AttributeConst.REP_SHOW_UNREAD,isShow);             //未読コメントのある日報のみを表示するかどうか
        putRequestScope(AttributeConst.REP_EXIST_UNREAD,exist_unread);      //未読コメントがついた日報が存在するかどうか
        putRequestScope(AttributeConst.REACTIONS,reactions);            //リアクション数のリスト


        //セッションスコープ内の日報データを削除
        removeSessionScope_report();
        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH,flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_TOP_INDEX);
    }

    /**
     * 出勤処理を行い一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void punchIn() throws ServletException,IOException{
        //CSRF対策用 tokenチェック
        if(checkToken()) {
            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);

            //現在時刻をユーザーテンプデータの出勤時刻に保存する
            tmpService.setPunchIn(loginEmployee, CalculationUtil.NowFormatTime());

            //出勤登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH,MessageConst.I_PUNCH_IN.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_TOP,ForwardConst.CMD_INDEX);
        }
    }

    /**
     * 出勤処理をキャンセルし一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void punchIn_cancel() throws ServletException,IOException{
        //CSRF対策用 tokenチェック
        if(checkToken()) {
            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView)getSessionScope(AttributeConst.LOGIN_EMP);

            //ユーザーテンプデータの出勤時刻にnullを設定する
            tmpService.setPunchIn(loginEmployee, null);

            //出勤キャンセル完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH,MessageConst.I_PUNCH_IN_CANCEL.getMessage());

            //一覧画面をリダイレクト
            redirect(ForwardConst.ACT_TOP,ForwardConst.CMD_INDEX);
        }
    }
}
