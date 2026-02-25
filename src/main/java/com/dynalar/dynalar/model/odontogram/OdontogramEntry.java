package com.dynalar.dynalar.model.odontogram;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "odontogram_entry")
public class OdontogramEntry {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JsonIgnore
	    private Odontogram odontogram;

	    @ManyToOne
	    private Tooth tooth;

	    @ManyToOne
	    private ToothSurface surface;

	    @ManyToOne
	    private Pathology pathology;
	    
	    @Enumerated(EnumType.STRING)
	    private ProcessType processType;
	    
	    
	    public OdontogramEntry() {
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Odontogram getOdontogram() {
			return odontogram;
		}

		public void setOdontogram(Odontogram odontogram) {
			this.odontogram = odontogram;
		}

		public Tooth getTooth() {
			return tooth;
		}

		public void setTooth(Tooth tooth) {
			this.tooth = tooth;
		}

		public ToothSurface getSurface() {
			return surface;
		}

		public void setSurface(ToothSurface surface) {
			this.surface = surface;
		}

		public Pathology getPathology() {
			return pathology;
		}

		public void setPathology(Pathology pathology) {
			this.pathology = pathology;
		}

		public ProcessType getProcess() {
			return processType;
		}
		
		public void setProcess(ProcessType process) {
			this.processType = process;
		}
	    
}
