package com.qoormthon.empty_wallet.domain.character.entity;

import com.qoormthon.empty_wallet.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "score",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_score_user", columnNames = "user_id")
        },
        indexes = {
                @Index(name = "idx_score_user", columnList = "user_id")
        }
)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_score_user"))
    private User user;

    @Column(name = "caf", nullable = false)
    private Long caf = 0L;

    @Column(name = "tax", nullable = false)
    private Long tax = 0L;

    @Column(name = "imp", nullable = false)
    private Long imp = 0L;

    @Column(name = "sub", nullable = false)
    private Long sub = 0L;

    @Column(name = "yolo", nullable = false)
    private Long yolo = 0L;

    @Column(name = "fash", nullable = false)
    private Long fash = 0L;

    protected Score() { }
    private Score(User user) {
        this.user = user;
    }

    public static Score of(User user) {
        return new Score(user);
    }

    // 점수 가산/감산 유틸
    public void addCaf(long delta)  { this.caf  += delta; }
    public void addTax(long delta)  { this.tax  += delta; }
    public void addImp(long delta)  { this.imp  += delta; }
    public void addSub(long delta)  { this.sub  += delta; }
    public void addYolo(long delta) { this.yolo += delta; }
    public void addFash(long delta)  { this.fash  += delta; }

    // 필요 시 게터들
    public Long getId()   { return id; }
    public User getUser() { return user; }
    public Long getCaf()  { return caf; }
    public Long getTax()  { return tax; }
    public Long getImp()  { return imp; }
    public Long getSub()  { return sub; }
    public Long getYolo() { return yolo; }
    public Long getFash()  { return fash; }

    public void overwrite(long caf, long tax, long imp, long sub, long yolo, long fash) {
        this.caf  = caf;
        this.tax  = tax;
        this.imp  = imp;
        this.sub  = sub;
        this.yolo = yolo;
        this.fash  = fash;
    }
}
