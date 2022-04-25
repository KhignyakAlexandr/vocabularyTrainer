package com.alexh.vocabtrainer.entities;

import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

/**
 * Abstraction for all persistence entities.
 * <p>
 * Contains commons fields like ID, CREATED and LAST_MODIFIED.
 * Automatically sets CREATED and updates LAST_MODIFIED fields.
 * <p>
 * Defines <i>equals()</i> and <i>hashCode</i> based on ID.
 * <p>
 * Created by User on 08-Aug-17.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AbstractEntity implements Serializable{

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    Date lastModified;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    Date created;

    @PrePersist
    @PreUpdate
    void setLastModified() {
        Date now = new Date();
        lastModified = now;

        if (created == null) {
            created = now;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (id == null || obj == null) {
            return false;
        }

        if (!(obj instanceof AbstractEntity)) {
            return false;
        }

        return ((AbstractEntity) obj).id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
