package com.example.tradeofthehouse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "politicians")
public class Politician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "politician_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "party", length = 50)
    private String party;

    @Column(name = "state", length = 50)
    private String state;

    public Politician() {}

    public Politician(String name, String party, String state) {
        this.name = name;
        this.party = party;
        this.state = state;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return String.format("Politician{id=%d, name='%s', party='%s', state='%s'}",
                             id, name, party, state);
    }
}
