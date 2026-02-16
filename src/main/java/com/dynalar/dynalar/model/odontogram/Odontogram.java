package com.dynalar.dynalar.model.odontogram;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "odontogram")
public class Odontogram {

	private LocalDateTime creationDate;

    @OneToMany(mappedBy = "odontogram")
    private List<Tooth> teeth;
}
