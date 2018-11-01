package com.singer.personal;

public class Equipement {
	private String nom;
	private String id_equipement;
	public Equipement(String nom, String id_equipement) {
		super();
		this.nom = nom;
		this.id_equipement = id_equipement;
	}
	
	public Equipement(String id_equipement) {
		this.id_equipement = id_equipement;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getId_equipement() {
		return id_equipement;
	}

	public void setId_equipement(String id_equipement) {
		this.id_equipement = id_equipement;
	}

	public boolean isTheSame(Equipement equipement2){
		if(this.id_equipement.equals(equipement2.id_equipement)){
			return true;
		}
		else{
			return false;
		}
	}
}
