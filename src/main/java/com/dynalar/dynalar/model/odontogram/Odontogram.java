package com.dynalar.dynalar.model.odontogram;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "odontogram")
public class Odontogram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime creationDate;

    @OneToMany(mappedBy = "odontogram")
    private List<Tooth> teeth;
    
    public Odontogram() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public List<Tooth> getTeeth() {
		return teeth;
	}

	public void setTeeth(List<Tooth> teeth) {
		this.teeth = teeth;
	}
    
    
}
