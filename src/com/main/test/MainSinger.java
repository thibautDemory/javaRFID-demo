package com.main.test;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import com.module.interaction.RXTXListener;
import com.module.interaction.ReaderHelper;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.singer.personal.Equipement;
import com.singer.personal.Humain;
import com.singer.personal.SetEquipement;
import com.util.StringTool;

public class MainSinger {
	static ArrayList<Humain> humains_presents = new ArrayList<Humain>();
	static ArrayList<Equipement> equipements_presents = new ArrayList<Equipement>();
	static ArrayList<Humain> humains_BDD = new ArrayList<>();
	static ArrayList<Equipement> equipements_BDD = new ArrayList<>();
	static ArrayList<SetEquipement> setEquipements_BDD = new ArrayList<>();
	static ReaderHelper mReaderHelper;
	
	static Observer mObserver = new RXObserver() {
		@Override
		protected void onInventoryTag(RXInventoryTag tag) {
			System.out.println("EPC data:" + tag.strEPC);
			process_tag(tag.strEPC);
			
		}
		
		@Override
		protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
			System.out.println("inventory end:" + endTag.mTotalRead);
			((RFIDReaderHelper) mReaderHelper).realTimeInventory((byte) 0xff,(byte)0x01);
		}
		
		@Override
		protected void onExeCMDStatus(byte cmd,byte status) {
			System.out.format("CDM:%s  Execute status:%S", 
					String.format("%02X",cmd),String.format("%02x", status));
		}
		
		
		
	};
	
	static Observer mObserver1 = new RXObserver() {
		@Override
		protected void onInventoryTag(RXInventoryTag tag) {
			System.out.println("EPC data:" + tag.strEPC);
		}
		
		@Override
		protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
			System.out.println("inventory end:" + endTag.mTotalRead);
			((RFIDReaderHelper) mReaderHelper).realTimeInventory((byte) 0xff,(byte)0x01);
		}
		
		@Override
		protected void onExeCMDStatus(byte cmd,byte status) {
			System.out.format("CDM:%s  Execute status:%S", 
					String.format("%02X",cmd),String.format("%02x", status));
		}
	};
	
	static RXTXListener mListener = new RXTXListener() {
		@Override
		public void reciveData(byte[] btAryReceiveData) {
			// TODO Auto-generated method stub
			System.out.println("reciveData" + StringTool.byteArrayToString(btAryReceiveData, 0, btAryReceiveData.length));
		}

		@Override
		public void sendData(byte[] btArySendData) {
			// TODO Auto-generated method stub
			System.out.println("sendData" + StringTool.byteArrayToString(btArySendData, 0, btArySendData.length));
		}

		@Override
		public void onLostConnect() {
			// TODO Auto-generated method stub
		}
		
	};
	/***
	 * Fonction qui met en route l'antenne donn�e en parametre
	 * @param idAntenne (antenne 1: 0x00, antenne 2: 0x01,antenne 3: 0x02,antenne 4: 0x03) 
	 */
	public static void setWorkAntenna(byte idAntenne) {
		((RFIDReaderHelper) mReaderHelper).setWorkAntenna((byte) 0x01, (byte) idAntenne);
	}
	/***
	 * fonction qui commence par mettre en route les antennes puis fait une lecture des tags pour le temps indiqué par @secondes
	 * @param secondes temps de lecture
	 * @param antennes liste des antennes à mettre en route (antenne 1: 0x00, antenne 2: 0x01,antenne 3: 0x02,antenne 4: 0x03)
	 */
	public static void realTimeInventory(int secondes, byte[] antennes) {
		for (byte antenne : antennes) {
			setWorkAntenna(antenne);
		}
		((RFIDReaderHelper) mReaderHelper).realTimeInventory((byte) 0x01, (byte) 0xff);
		try {
			TimeUnit.SECONDS.sleep(secondes);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Processus qui traite les tags que l'on repère.
	 * On cherche d'abord a connaitre son type, c'est à dire est ce que c'est un humain ou un equipement, si ce n'est aucun des 2 c'est une erreur.
	 * Le processus marche ensuite de la meme manière, on regarde si l'humain ou l'équipement est déjà présent dans la mémoire tampon, si ce n'est pas
	 * le cas on le rajoute.
	 * @param epc donnée en brut qui arrive à l'ordinateur
	 */
	public static void process_tag(String epc) {
		int len_epc = epc.length();
		String type = epc.substring(len_epc-4, len_epc-2);
		String id = epc.substring(len_epc-2,len_epc);
		if (type.equals("00")){
			Humain nouvelhumain = new Humain(id);
			boolean humain_deja_present = false;
			for (int i=0;i<humains_presents.size();i++){
				if (humains_presents.get(i).isTheSame(nouvelhumain)){
					humain_deja_present = true;
				}
			}
			if (!humain_deja_present){
				humains_presents.add(nouvelhumain);
			}
		}else if (type.equals("01")){
			Equipement nouvelEquipement = new Equipement(id);
			boolean equipement_deja_present = false;
			for (int i =0;i< equipements_presents.size();i++){
				if (equipements_presents.get(i).isTheSame(nouvelEquipement)){
					equipement_deja_present = true;
				}
			}
			if (!equipement_deja_present){
				equipements_presents.add(nouvelEquipement);
			}
		}else{
			System.out.println("Erreur: le tag reperé n'est ni un humain ni un equipement.");
		}
		
	}
	/***
	 * fonction qui retourne vrai si au moins un humain est présent dans la liste des tags. retourne faux sinon
	 * @return présence d'un humain
	 */
	public static boolean isHumanPresent() {
		if (humains_presents.size()>0){
			return true;
		}else{
			return false;
		}
	}
	/***
	 * retourne vrai si il y a un seul humain, faux si il y en a plusieurs ou 0
	 * @return la présence d'un seul humain
	 */
	public static boolean onlyOneHuman() {
		if (humains_presents.size() == 1){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * retourne un humain sélectionner via son ID
	 * @param id identifiant de l'humain
	 * @return un humain en particulier
	 */
	public static Humain getHumainById(String id){
		int nb_humains = humains_BDD.size();
		for (int i =0; i < nb_humains; i++){
			if (humains_BDD.get(i).getId_humain().equals(id)){
				return humains_BDD.get(i);
			}
		}
		System.out.println("humain introuvable par ID");
		return null;
	}

	/**
	 * Fonction qui renvoie vraie si la protection en parametre est présente dans les équipements reperé par l'antenne
	 * @param protection La protection dont on veut connaitre la présence ou non
	 * @return vraie si la protection a été reperée, faux sinon
	 */
	private static boolean is_protection_present(Equipement protection){
		int nb_equipements = equipements_presents.size();
		for (int i =0; i< nb_equipements; i++){
			if (equipements_presents.get(i).isTheSame(protection)){
				return true;
			}
		}
		return false;
	}

	/***
	 * fonction qui permet de dire si une protection est manquante sur l'employé.
	 * on regarde les équipements du Set un par un, et on vérifie à chaque fois que l'équipement en question est bien présent.
	 * @param set_equipement L'ensemble des equipements qu'est sensé porté l'employé
	 * @return vrai si une protection est manquante, faux si l'employé porte bien tous ses équipements.
	 */
	private static boolean is_protection_missing(SetEquipement set_equipement) {
		int nb_equipements = set_equipement.getLes_equipements_du_set().size();
		for (int i =0 ; i< nb_equipements; i++){
			if (!is_protection_present(set_equipement.getLes_equipements_du_set().get(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Fait patienter le programme pendant un certain nombre de secondes
	 * @param secondes le nombre de secondes que l'on patiente
	 */
	private static void dormir(int secondes) {
		try {
			TimeUnit.SECONDS.sleep(secondes);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * met à zéro les deux mémoires tampons
	 */
	private static void reset_la_memoire() {
		equipements_presents.clear();
		humains_presents.clear();
	}
	
	public static void main(String[] args) {

		//initialisation base de données:
		// cette partie du code est à remplacer par la connexion avec la base de donnée
		equipements_BDD.add(new Equipement("gants","01"));
		equipements_BDD.add(new Equipement("veste","02"));
		equipements_BDD.add(new Equipement("casquette","03"));
		equipements_BDD.add(new Equipement("chaussures","04"));

		ArrayList<Equipement> setn1= new ArrayList<>();
		setn1.add(equipements_BDD.get(0));
		setn1.add(equipements_BDD.get(1));
		setEquipements_BDD.add(new SetEquipement(setn1,"plombier"));

		ArrayList<Equipement> setn2= new ArrayList<>();
		setn2.add(equipements_BDD.get(1));
		setn2.add(equipements_BDD.get(2));
		setn2.add(equipements_BDD.get(3));
		setEquipements_BDD.add(new SetEquipement(setn2,"carreleur"));

		humains_BDD.add(new Humain("Demory","thibaut",setEquipements_BDD.get(0),"01"));
		humains_BDD.add(new Humain("Biau Peyret","anthime",setEquipements_BDD.get(1),"02"));




		// étape de la conexion de l'antenne
		final ReaderConnector mConnector = new ReaderConnector();
		mReaderHelper = mConnector.connectCom("COM3", 115200);
		if(mReaderHelper != null) {
			System.out.println("Connect success!");
			try {
				mReaderHelper.registerObserver(mObserver);
				mReaderHelper.registerObserver(mObserver1);
				mReaderHelper.setRXTXListener(mListener);
				((RFIDReaderHelper) mReaderHelper).getTagMask((byte) 0xff);
				
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		} else {
			System.out.println("Connect faild!");
			mConnector.disConnect();
		} 
		//initialisation des paramètres
		int max_loops = 5;
		int over_time = 3;
		int nb_tours = 0;
		int nb_relecture_manquant= 0;
		int nb_lectures = 0;
		int nb_min_tags = 3;
		int temps_lecture = 4;
		int nb_tags_detectes = 0;
		byte[] les_antennes = new byte[] {0x00,0x01};
		
		
		boolean flag = true;
		while (flag == true) { // à voir si on veut sortir de la boucle à un moment donné, peut etre via une touche
			if (nb_tours > max_loops) {
				reset_la_memoire();
				nb_tours = 0;
			}else {
				nb_tours ++;
			}
			//On fait une première lecture
			realTimeInventory(temps_lecture,les_antennes);
			nb_tags_detectes = equipements_presents.size()+humains_presents.size();
			nb_lectures = 0;
			while ((nb_tags_detectes < nb_min_tags) & (nb_lectures< max_loops)){
				realTimeInventory(temps_lecture,les_antennes);
				nb_tags_detectes = equipements_presents.size() + humains_presents.size();
				nb_lectures ++;
			}
			if (isHumanPresent()) {
				if(onlyOneHuman()) {
					Humain personne_sous_le_portique = humains_presents.get(0); // la liste n'est sensé avoir qu'un seul élement, l'humain qui se trouve sous le portique
					String nom_humain = personne_sous_le_portique.getNom();
					String prenom_humain = personne_sous_le_portique.getPrenom();
					SetEquipement profession = personne_sous_le_portique.getProfession();
					int nb_equipements_dans_le_set = profession.getLes_equipements_du_set().size();
					System.out.println("Bonjour "+prenom_humain +" "+ nom_humain+", votre profession est : "+profession.getNom_du_set()+".");

					boolean equipement_manquant = is_protection_missing(profession);

					nb_relecture_manquant = 0;

					while (equipement_manquant &&  nb_relecture_manquant<max_loops){
						nb_relecture_manquant++;
						realTimeInventory(2,les_antennes);
						equipement_manquant= is_protection_missing(profession);
						System.out.println("il vous manque : ");
						for (int i=0; i<nb_equipements_dans_le_set; i++){
							if (!is_protection_present(profession.getLes_equipements_du_set().get(i))){
								System.out.println("- "+profession.getLes_equipements_du_set().get(i).getNom());
							}
						}
					}

					if (equipement_manquant){
						System.out.println("Désolé, nous n'avons pas réussi à trouver tous vos équipements, pour rappel il vous manque: ");
						for (int i=0; i<nb_equipements_dans_le_set; i++){
							if (!is_protection_present(profession.getLes_equipements_du_set().get(i))){
								System.out.println("- "+profession.getLes_equipements_du_set().get(i).getNom());
							}
						}
						dormir(3);// on laisse le temps à l'humain de partir
						reset_la_memoire();
						nb_tours = 0;

					}else{
						System.out.println("Bravo vous avez tous vos équipements c'est génial.");
						dormir(3);
						reset_la_memoire();
						nb_tours = 0;
					}



				}else{
					System.out.println("Il y a trop d'humains ici, il faut que quelquun recule");
					dormir(2);// on laisse le temps à l'humain en trop de reculer
					reset_la_memoire();
					nb_tours=0;
				}
			}else{
				System.out.println("Aucun humain de detecté");
			}
			
			
		}
			
		mConnector.disConnect();

		System.out.println("fin!");
	}




}
