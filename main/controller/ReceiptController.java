package main.controller;

import main.entity.Receipt;
import main.entity.User.Applicant;

/**
 * Controller class for managing receipt-related operations.
 */
public class ReceiptController {

    /**
     * Creates a new receipt for the given applicant.
     *
     * @param applicant The applicant for whom the receipt is generated.
     * @return A new {@link Receipt} object.
     */
    public static Receipt createReceipt(Applicant applicant) {
        return new Receipt(applicant);
    }

    /**
     * Displays the generated receipt details on the console.
     *
     * @param receipt The {@link Receipt} object to display.
     */
    public static void displayReceipt(Receipt receipt) {
        if (receipt != null) {
            System.out.println(receipt.generateReceipt());
        } else {
            System.out.println("No receipt found.");
        }
    }
}
