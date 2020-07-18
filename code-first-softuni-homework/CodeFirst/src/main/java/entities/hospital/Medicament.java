package entities.hospital;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "prescribed_medicaments", schema = "hospital", catalog = "hospital")
public class Medicament extends BaseEntity {
    private String name;
    private Patient patient;
    private Set<Diagnose> diagnoses;

    @ManyToMany(mappedBy = "medicaments")
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @ManyToOne
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medicament() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isEmpty()) {
        this.name = name;}
    }
}
