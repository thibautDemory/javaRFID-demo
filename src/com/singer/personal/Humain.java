package com.singer.personal;


public class Humain {
	private String nom;
	private String prenom;
	private SetEquipement profession;
	private String id_humain;
	
	public String getId_humain() {
		return id_humain;
	}
	public void setId_humain(String id_humain) {
		this.id_humain = id_humain;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public SetEquipement getProfession() {
		return profession;
	}
	public void setProfession(SetEquipement profession) {
		this.profession = profession;
	}
	public Humain(String nom, String prenom, SetEquipement profession, String id_humain) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.profession = profession;
		this.id_humain = id_humain;
	}
	public Humain(String id_humain) {
		this.id_humain = id_humain;
	}

	public boolean isTheSame(Humain humain2){
		if (this.id_humain.equals(humain2.id_humain)){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	

}
