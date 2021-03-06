package actions.views;


import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Report;

/**
 * 日報データのDTOモデル⇔Viewモデルの変換を行うクラス
 * @author ryouta.osada
 *
 */
public class ReportConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv ReportViewのインスタンス
     * @return Reportのインスタンス
     */
    public static Report toModel(ReportView rv) {
        return new Report(
                rv.getId(),
                EmployeeConverter.toModel(rv.getEmployee()),
                rv.getReportDate(),
                rv.getTitle(),
                rv.getContent(),
                rv.getCreatedAt(),
                rv.getUpdatedAt(),
                rv.getPunchIn(),
                rv.getPunchOut(),
                rv.getCommentCount(),
                rv.getIsReadComment() == null
                    ? null
                    : rv.getIsReadComment() == AttributeConst.READ_FLAG_TRUE.getIntegerValue()
                        ? JpaConst.REP_READ_CMT_TRUE
                        : JpaConst.REP_READ_CMT_FALSE);
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param r Reportのインスタンス
     * @return ReportViewのインスタンス
     */
    public static ReportView toView(Report r) {
        if(r == null) {
            return null;
        }

        return new ReportView(
                r.getId(),
                EmployeeConverter.toView(r.getEmployee()),
                r.getReportDate(),
                r.getTitle(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getPunchIn(),
                r.getPunchOut(),
                r.getCommentCount(),
                r.getIsReadComment() == null
                    ? null
                    : r.getIsReadComment() == JpaConst.REP_READ_CMT_TRUE
                        ? AttributeConst.READ_FLAG_TRUE.getIntegerValue()
                        : AttributeConst.READ_FLAG_FALSE.getIntegerValue());
    }


    public static List<ReportView> toViewList(List<Report> list){
        List<ReportView> evs = new ArrayList<>();

        for(Report r: list) {
            evs.add(toView(r));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Report r,ReportView rv) {
        r.setId(rv.getId());
        r.setEmployee(EmployeeConverter.toModel(rv.getEmployee()));
        r.setReportDate(rv.getReportDate());
        r.setTitle(rv.getTitle());
        r.setContent(rv.getContent());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
        r.setPunchIn(rv.getPunchIn());
        r.setPunchOut(rv.getPunchOut());
        r.setCommentCount(rv.getCommentCount());
        r.setIsReadComment(rv.getIsReadComment());
    }
}
