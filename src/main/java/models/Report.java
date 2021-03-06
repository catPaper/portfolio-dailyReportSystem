package models;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報管理のDTOモデル
 * @author ryouta.osada
 *
 */
@Table(name = JpaConst.TABLE_REP)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_REP_GET_ALL,
            query = JpaConst.Q_REP_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_REP_COUNT,
            query = JpaConst.Q_REP_COUNT_DEF),
    @NamedQuery(
            name = JpaConst.Q_REP_COUNT_ALL_MINE,
            query = JpaConst.Q_REP_COUNT_ALL_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_REP_GET_ALL_MINE,
            query = JpaConst.Q_REP_GET_ALL_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_REP_GET_ALL_MINE_UNREAD,
            query = JpaConst.Q_REP_GET_ALL_MINE_UNREAD_DEF),
    @NamedQuery(
            name = JpaConst.Q_REP_COUNT_ALL_MINE_UNREAD,
            query = JpaConst.Q_REP_COUNT_ALL_MINE_UNREAD_DEF)
})
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしのコンストラクタを自動生成する(Lombok)
@AllArgsConstructor
@Entity
public class Report {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.REP_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 日報を登録した従業員
     */
    @ManyToOne
    @JoinColumn(name= JpaConst.REP_COL_EMP,nullable = false)
    private Employee employee;

    /**
     * いつの日報かを示す日付
     */
    @Column(name = JpaConst.REP_COL_REP_DATE,nullable = false)
    private LocalDate reportDate;

    /**
     * 日報のタイトル
     */
    @Column(name = JpaConst.REP_COL_TITLE,length = 255,nullable = false)
    private String title;

    /**
     * 日報の内容
     */
    @Lob
    @Column(name = JpaConst.REP_COL_CONTENT,nullable = false)
    private String content;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.REP_COL_CREATED_AT,nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.REP_COL_UPDATED_AT,nullable = false)
    private LocalDateTime updatedAt;

    /*
     * 出勤時刻
     */
    @Column(name = JpaConst.REP_COL_PUNCH_IN,nullable = false)
    private Time punchIn;

    /*
     * 退勤時刻
     */
    @Column(name = JpaConst.REP_COL_PUNCH_OUT,nullable = false)
    private Time punchOut;

    /**
     * 日報についたコメントの数
     */
    @Column(name = JpaConst.REP_COL_COMMENT_COUNT,nullable = false,columnDefinition = "integer default 0")
    private Integer commentCount;

    /**
     * 新しいコメントを見たか
     */
    @Column(name = JpaConst.REP_COL_IS_READ_COMMENT,nullable = false,columnDefinition = "integer default 1")
    private Integer isReadComment;

}
