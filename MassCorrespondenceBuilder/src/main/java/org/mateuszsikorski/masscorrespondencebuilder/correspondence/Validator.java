package org.mateuszsikorski.masscorrespondencebuilder.correspondence;


public class Validator {
	
	// mozna by to zrefaktorowac tak zeby uzyskac metody do kontroli poszczegolnych danych i w metodach
	// validacji je tylko wywolywac, ale nie mialem czasu na kwestie kosmetyczne

	public boolean validate(Sender sender) {
		if(sender.getSendingDate().length() > 10 || 
				sender.getSendingDate().length() < 8)//dd.mm.yyyy = 10 dd.MM.yyyy = 9/10
			return false;
		if(sender.getCompanyName().length() <= 4 || //mysle ze to dobry warunek, nawet skrotowa nazwa nie bedzie tak krotka
				sender.getCompanyName().matches(".*\\d.*")) // w nazwie firmy CHYBA nie moga wystapic liczby
			return false;
		if(!sender.getCompanyStreet().matches(".*\\d.*")) // sprawdzenie czy w adresie jest nr budynku
			return false;
		if(!(sender.getCompanyPostalCode().charAt(2) == '-')) // poprawnosc kodu pocztowego
			return false;
		if(sender.getCompanyCity().length() <= 2 || // 3 literowe miasta istnieja, 2literowego nigdy nie widzialem
				sender.getCompanyCity().matches(".*\\d.*")) // liczba nie moze wystapic w nazwie miasta
			return false;
		
		// Zakladam ze nie wszystkie firmy moga chciec umieszczac jakies informacje w stopce
		
		return true;
	}
	
	public boolean validate(Recipient recipient) {
		if(recipient.getFirstName().length() <= 2 || 
				recipient.getFirstName().matches(".*\\d.*"))
			return false;
		if(recipient.getLastName().length() <= 2 || 
				recipient.getLastName().matches(".*\\d.*"))
			return false;
		if(!recipient.getStreet().matches(".*\\d.*")) 
			return false;
		if(!(recipient.getPostalCode().charAt(2) == '-'))
			return false;
		if(recipient.getCity().length() <= 2 || 
				recipient.getCity().matches(".*\\d.*")) 
			return false;
		
		return true;
	}
	
	public boolean validate(Message message) {
		
		if(message.getParagraph1().length() <= 50) // wstep
			return false;
		if(message.getParagraph2().length() <= 150) // rozwiniecie
			return false;
		if(message.getParagraph2().length() <= 50) // zakonczenie
			return false;
		
		return true;
	}
}
