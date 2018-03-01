package net.frozenchaos.catcoop.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "images")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(nullable = false, updatable = false)
    private byte[] data;

    public Image(byte[] data) {
        this.timestamp = new Date();
        this.data = data;
    }
}
