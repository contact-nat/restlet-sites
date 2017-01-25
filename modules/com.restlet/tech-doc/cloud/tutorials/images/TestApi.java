
import net.apispark.webapi.client.ContactListClientResource;
import net.apispark.webapi.representation.Contact;
import net.apispark.webapi.representation.ContactList;

public class TestApi {

	public static void main(String[] args) {
		// Instantiate resource /contacts/ 
		ContactListClientResource clcr = new ContactListClientResource();
		try{
			// GET request on  /contacts to retrieve each stored Contact
			ContactList myContacts = clcr.represent();
			//Iterate through Contact representations
			for (Contact contact : myContacts) {
				System.out.println(String.format("%s %s %s", contact.getId(), contact.getFirstName(), contact.getLastName()));
			}
			
			//Instantiate a new Contact representation
			Contact newContact = new Contact();
			// Check that you set each required property
			newContact.setFirstName("Clark");
			newContact.setLastName("Kent");
			// POST request on  /contacts to store your Contact
			clcr.add(newContact);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

}
