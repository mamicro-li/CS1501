// Sharon Gao
// CS 1501, Project 3

import java.util.*;
import java.lang.Math;

class Listing {
	String street_address;
	String city;
	int apt_num;
	int zip;
	int rent; 
	int square_footage;
	
	// Constructor
	public Listing(String street_address, String city, int apt_num, int zip, int rent, int square_footage) {
		this.street_address = street_address;
		this.city = city;
		this.apt_num = apt_num;
		this.zip = zip;
		this.rent = rent;
		this.square_footage = square_footage;
	}
	
	// Accessor and mutator methods for Listing variables
	public void setSA(String new_address) {
		this.street_address = new_address;
	}
	
	public String getSA() {
		return this.street_address;
	}
	public void setCity(String new_city) {
		this.city = new_city;
	}
	
	public String getCity() {
		return this.city;
	}
	public void setAN(int new_aptnum) {
		this.apt_num = new_aptnum;
	}
	
	public int getAN() {
		return this.apt_num;
	}
	public void setZip(int new_zip) {
		this.zip = new_zip;
	}
	
	public int getZip() {
		return this.zip;
	}
	public void setRent(int new_price) {
		this.rent = new_price;
	}
	
	public int getRent() {
		return rent;
	}
	
	public void setSF(int new_sf) {
		this.square_footage = new_sf;
	}
	
	public int getSF() {
		return this.square_footage;
	}
	
	// Listing toString method
	public String toString() {
		return "Street Address: " + street_address + "\nCity: " + city + "\nApartment Number: " + apt_num + "\nZip Code: " + zip + "\nRent per Month: " + rent + "\nSquare Footage: " + square_footage;
	}
}

// Comparator class for Listing rent values	
class compareRent implements Comparator<Listing> {
		public int compare(Listing one, Listing two) {
			return one.rent - two.rent;
		}
	}
// Comparator class for Listing square footage values
class compareSF implements Comparator<Listing> {
		public int compare(Listing one, Listing two) {
			return -1 * (one.square_footage - two.square_footage);
		}

}



public class AptTracker {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		char cont;
		compareRent cr = new compareRent();
		compareSF csf = new compareSF();
		int index;
		int counter = 0;
		ArrayList<String> cities = new ArrayList<String>(10);
		@SuppressWarnings("unchecked")
		PQ<Listing>[] city_rent = new PQ[100];
		@SuppressWarnings("unchecked")
		PQ<Listing>[] city_sf = new PQ[100];
		PQ<Listing> pq_rent = new PQ<Listing>(100, cr);
		PQ<Listing> pq_sf = new PQ<Listing>(100, csf);
		String c, sa;
		int an, z, r, sf;
		int i;
		boolean found;
		System.out.println("-----------------------");
		System.out.println("|  Apartment Tracker  |");
		System.out.print("-----------------------");
		do {
			System.out.println("\n1. Add Apartment Listing");
			System.out.println("2. Update Apartment Listing");
			System.out.println("3. Remove Apartment Listing");
			System.out.println("4. Retrieve Lowest Cost Apartment Listing");
			System.out.println("5. Retrieve Highest Square Footage Apartment Listing");
			System.out.println("6. Retrieve Lowest Cost Apartment Listing by City");
			System.out.println("7. Retrieve Highest Square Footage Apartment Listing by City");
			System.out.print("Select an option: ");
			int choice = scan.nextInt();
			switch(choice) {
				case 1:		// Add Apartment Listing
					System.out.println("\nPlease input information about the new apartment listing: ");
					System.out.print("City: ");
					c = scan.next();
					scan.nextLine();
					System.out.print("Street Address: ");
					sa = scan.nextLine();
					System.out.print("Apartment Number: ");
					an = scan.nextInt();
					scan.nextLine();
					System.out.print("Zip Code: ");
					z = scan.nextInt();
					scan.nextLine();
					while ((int)Math.log10(z) + 1 != 5) {
						System.out.print("Invalid zip code. Please enter a valid 5-digit input: ");
						z = scan.nextInt();
					}
					System.out.print("Rent per Month: ");
					r = scan.nextInt();
					scan.nextLine();
					System.out.print("Square Footage: ");
					sf = scan.nextInt();
					scan.nextLine();
					Listing newApt = new Listing(sa, c, an, z, r, sf);
					while(pq_rent.contains(counter)) {
						counter++;
					}
					pq_rent.insert(counter, newApt);
					pq_sf.insert(counter, newApt);
					counter = 0;
					if (!cities.contains(c)) {	
						cities.add(c);
						PQ<Listing> new_rent = new PQ<Listing>(10, cr);
						new_rent.insert(0, newApt);
						city_rent[cities.indexOf(c)] = new_rent;
						PQ<Listing> new_sf = new PQ<Listing>(10, csf);
						new_sf.insert(0, newApt);
						city_sf[cities.indexOf(c)] = new_sf;
					}
					else {
						index = cities.indexOf(c);	// Retrieve index using city name
						while(city_rent[index].contains(counter)) {
							counter++;	// Look for unused index
						}
						city_rent[index].insert(counter, newApt);	// Use index to insert into city priority queue
						city_sf[index].insert(counter, newApt);
						counter = 0;
					}
					break;
				case 2:		// Update Apartment Listing
					if (pq_rent.isEmpty()) {
						System.out.println("\nNo listings to update. Please add an apartment listing before attempting update.");
						break;
					} 
					System.out.println("\nPlease input information about an existing apartment listing to update: ");
					System.out.print("Street Address: ");
					scan.nextLine();
					sa = scan.nextLine();
					System.out.print("Apartment Number: ");
					an = scan.nextInt();
					scan.nextLine();
					System.out.print("Zip Code: ");
					z = scan.nextInt();
					while ((int)Math.log10(z) + 1 != 5) {
						System.out.print("Invalid zip code. Please enter a valid 5-digit input: ");
						z = scan.nextInt();
					}
					scan.nextLine();
					found = false;
					for (i = 0; i < pq_rent.size(); i++) {		// Search through rent PQ for match
						if (pq_rent.keyOf(i).getSA().equals(sa) && pq_rent.keyOf(i).getAN() == an && pq_rent.keyOf(i).getZip() == z) {
							found = true;
							break;
						}
					}
					if (found = false) {
						System.out.println("No listing with that address was found.");
						break;
					}	
					System.out.print("Enter an updated price for this listing: ");
					r = scan.nextInt();
					pq_rent.keyOf(i).setRent(r);
					System.out.print("Listing updated!");
					break;
				case 3:		// Remove Apartment Listing
					if (pq_rent.isEmpty()) {
						System.out.println("\nNo listings to remove. Please add an apartment listing before attempting removal.");
						break;
					} 
					System.out.println("\nPlease input information about an existing apartment listing to remove: ");
					System.out.print("Street Address: ");
					scan.nextLine();
					sa = scan.nextLine();
					System.out.print("Apartment Number: ");
					an = scan.nextInt();
					scan.nextLine();
					System.out.print("Zip Code: ");
					z = scan.nextInt();
					while ((int)Math.log10(z) + 1 != 5) {
						System.out.print("Invalid zip code. Please enter a valid 5-digit input: ");
						z = scan.nextInt();
					}
					scan.nextLine();
					found = false;
					for (i = 0; i < pq_rent.size(); i++) {	// Search through rent PQ for match and delete
						if (pq_rent.keyOf(i).getSA().equals(sa) && pq_rent.keyOf(i).getAN() == an && pq_rent.keyOf(i).getZip() == z) {
							pq_rent.delete(i);
							System.out.println("Listing removed!");
							found = true;
							break;
						}
					}
					if (found == false) {
						System.out.println("No listing with that address was found.");
						break;
					}	
					
					break;
				case 4:		// Retrieve Lowest Price Apartment
					System.out.println("\nRetrieving lowest priced apartment listing: ");
					System.out.println(pq_rent.minKey());
					break;
				case 5:		// Retrieve Hight Square Footage Apartment
					System.out.println("\nRetrieving highest square footage apartment listing:");
					System.out.println(pq_sf.minKey());
					break;
				case 6:		// Retrieve Lowest Price Apartment by City
					System.out.print("\nPlease input a city: ");
					c = scan.next();
					if (cities.contains(c)) {
						index = cities.indexOf(c);
						System.out.println("The lowest priced apartment in city " + c + ":");
						System.out.println(city_rent[index].minKey());
					}
					else {
						System.out.println("There are no apartments for " + c + " currently listed.");
						break;
					}
					break;
				case 7:		// Retrieve Hight Square Footage Apartment by City
					System.out.print("\nPlease input a city: ");
					c = scan.next();
					if (cities.contains(c)) {
						index = cities.indexOf(c);
						System.out.println("The highest square footage apartment in city " + c + ":");
						System.out.println(city_sf[index].minKey());
					}
					else {
						System.out.println("There are no apartments for " + c + " currently listed.");
						break;
					}
					break;
				default:
					System.out.println("Invalid Selection. Please select an option listed in the menu.");
					break;
			}
			System.out.print("\nDo you want to continue? (\'C\' to continue): ");
			cont = scan.next().charAt(0);
		} while (cont == 'C' || cont == 'c');
		
	}
}
