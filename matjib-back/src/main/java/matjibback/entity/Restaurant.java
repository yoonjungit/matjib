package matjibback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="res_id")
    private int id;

    @Column(name="res_name")
    private String resName;
    private String address;
    @Column(name="n_score")
    private float nScore;
    @Column(name="n_count")
    private int nCount;
    @Column(name="k_score")
    private float kScore;
    @Column(name="k_count")
    private int kCount;
    @Column(name="g_score")
    private float gScore;
    @Column(name="g_count")
    private int gCount;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name="avg_score")
    private float avgScore;
    @Column(name="latitude_y")
    private float latitude;
    @Column(name="longitude_x")
    private float longitude;
}
