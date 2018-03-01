package net.frozenchaos.catcoop.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "access")
public class AccessRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, updatable = false)
    private String cat;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "access_images", joinColumns = @JoinColumn(name = "access_fk"), inverseJoinColumns = @JoinColumn(name = "image_fk"))
    @OrderBy(value = "timestamp")
    private List<Image> images;

    public AccessRecord(String cat) {
        this.cat = cat;
        this.timestamp = new Date();
    }

    public AccessRecord(String cat, List<Image> images) {
        this.cat = cat;
        this.timestamp = new Date();
        this.images = new ArrayList<>(images);
    }

    public String getCat() {
        return cat;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<Image> getImages() {
        return new ArrayList<>(images);
    }

    public void addImage(Image image) {
        this.images.add(image);
    }
}
