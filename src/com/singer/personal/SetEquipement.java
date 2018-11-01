package com.singer.personal;

import java.util.ArrayList;

public class SetEquipement {
	private ArrayList<Equipement> les_equipements_du_set;
	private String nom_du_set;

	public SetEquipement(ArrayList<Equipement> les_equipements_du_set, String nom_du_set) {
		this.les_equipements_du_set = les_equipements_du_set;
		this.nom_du_set = nom_du_set;
	}

	public ArrayList<Equipement> getLes_equipements_du_set() {
		return les_equipements_du_set;
	}

	public void setLes_equipements_du_set(ArrayList<Equipement> les_equipements_du_set) {
		this.les_equipements_du_set = les_equipements_du_set;
	}

	public String getNom_du_set() {
		return nom_du_set;
	}

	public void setNom_du_set(String nom_du_set) {
		this.nom_du_set = nom_du_set;
	}

	public void addEquipementauSet(Equipement equipement){
		this.les_equipements_du_set.add(equipement);
	}
}
