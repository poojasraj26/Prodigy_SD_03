package core3;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ContactManagementSystem {
    private static final String CONTACTS_FILE = "contacts.ser";
    private Map<String, Contact> contacts = new HashMap<>();

    // Contact class to store name, phone, and email
    static class Contact implements Serializable {
        String phone;
        String email;

        Contact(String phone, String email) {
            this.phone = phone;
            this.email = email;
        }

        @Override
        public String toString() {
            return "Phone: " + phone + ", Email: " + email;
        }
    }

    // Load contacts from file
    @SuppressWarnings("unchecked")
    public void loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONTACTS_FILE))) {
            contacts = (HashMap<String, Contact>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No saved contacts found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading contacts.");
        }
    }

    // Save contacts to file
    public void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONTACTS_FILE))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving contacts.");
        }
    }

    // Add a new contact
    public void addContact(String name, String phone, String email) {
        contacts.put(name, new Contact(phone, email));
        saveContacts();
        System.out.println("Contact added successfully.");
    }

    // View all contacts
    public void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to display.");
        } else {
            contacts.forEach((name, contact) -> 
                System.out.println("Name: " + name + ", " + contact)
            );
        }
    }

    // Edit an existing contact
    public void editContact(String name, String phone, String email) {
        Contact contact = contacts.get(name);
        if (contact != null) {
            contact.phone = (phone.isEmpty()) ? contact.phone : phone;
            contact.email = (email.isEmpty()) ? contact.email : email;
            saveContacts();
            System.out.println("Contact updated successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    // Delete a contact
    public void deleteContact(String name) {
        if (contacts.remove(name) != null) {
            saveContacts();
            System.out.println("Contact deleted successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    // Main method to drive the application
    public static void main(String[] args) {
        ContactManagementSystem cms = new ContactManagementSystem();
        cms.loadContacts();
        Scanner scanner = new Scanner(System.in);
        String name, phone, email;
        int choice;

        do {
            System.out.println("\nContact Management System");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter phone number: ");
                    phone = scanner.nextLine();
                    System.out.print("Enter email address: ");
                    email = scanner.nextLine();
                    cms.addContact(name, phone, email);
                    break;

                case 2:
                    cms.viewContacts();
                    break;

                case 3:
                    System.out.print("Enter name of contact to edit: ");
                    name = scanner.nextLine();
                    System.out.print("Enter new phone number (leave blank to keep current): ");
                    phone = scanner.nextLine();
                    System.out.print("Enter new email address (leave blank to keep current): ");
                    email = scanner.nextLine();
                    cms.editContact(name, phone, email);
                    break;

                case 4:
                    System.out.print("Enter name of contact to delete: ");
                    name = scanner.nextLine();
                    cms.deleteContact(name);
                    break;

                case 5:
                    System.out.println("Exiting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}