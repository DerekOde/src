import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VendingMachineGUI {
    private SpecialVendingMachine currentMachine = null;

    public VendingMachineGUI() {
    }

    public void startMenu() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Vending Machine Interface");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 200);
                frame.setLayout(new GridLayout(3, 1));

                JButton createButton = new JButton("Create a New Vending Machine");
                JButton testButton = new JButton("Test Vending Machine");
                JButton maintenanceButton = new JButton("Maintenance");

                createButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine == null) {
                            createVendingMachine();
                        } else {
                            JOptionPane.showMessageDialog(frame, "A vending machine has already been created.");
                        }
                    }
                });
                testButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            testFeatures();
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });
                maintenanceButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            maintenance();
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });
                frame.add(createButton);
                frame.add(testButton);
                frame.add(maintenanceButton);
                frame.setVisible(true);
            }
        });
    }

    public void createVendingMachine() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Create Vending Machine");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(400, 200);
                frame.setLayout(new GridLayout(2, 2));

                JTextField slotCountField = new JTextField();
                JButton createButton = new JButton("Create");
                createButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int slotCount = Integer.parseInt(slotCountField.getText().trim());
                            currentMachine = new SpecialVendingMachine();
                            currentMachine.setSlots(slotCount);
                            addItemsToVendingMachine(); // New method call to add items directly in the GUI
                            frame.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number of slots.");
                        }
                    }
                });

                frame.add(new JLabel("Enter the number of slots:"));
                frame.add(slotCountField);
                frame.add(new JPanel()); // Placeholder for spacing
                frame.add(createButton);
                frame.setVisible(true);
            }
        });
    }

    // New method to add items directly in the GUI
    private void addItemsToVendingMachine() {
        JFrame addItemFrame = new JFrame("Add Items to Vending Machine");
        addItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addItemFrame.setSize(400, 400);
        addItemFrame.setLayout(new GridLayout(0, 2));

        JButton addItemButton = new JButton("Add Item");
        JButton finishButton = new JButton("Finish");

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField();
                JTextField caloriesField = new JTextField();
                JTextField priceField = new JTextField();

                Object[] message = {
                        "Name:", nameField,
                        "Calories:", caloriesField,
                        "Price:", priceField
                };

                int option = JOptionPane.showConfirmDialog(addItemFrame, message, "Add Item", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim();
                    String caloriesText = caloriesField.getText().trim();
                    String priceText = priceField.getText().trim();

                    if (name.isEmpty() || !name.matches("^[a-zA-Z]+$")) {
                        JOptionPane.showMessageDialog(addItemFrame, "Invalid name. Please enter a valid name (letters only).");
                    } else {
                        try {
                            int calories = Integer.parseInt(caloriesText);
                            double price = Double.parseDouble(priceText);
                            if (calories < 0 || price < 0) {
                                JOptionPane.showMessageDialog(addItemFrame, "Invalid calories or price. Please enter non-negative values.");
                            } else {
                                if (currentMachine.addItem(name, calories, price)) {
                                    JOptionPane.showMessageDialog(addItemFrame, "Item added successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(addItemFrame, "Item could not be added. It is either a duplicate item or the vending machine is full.");
                                }
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addItemFrame, "Invalid input. Please enter valid numbers for calories and price.");
                        }
                    }
                }
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show quantity input dialog for each item
                for (int i = 0; i < currentMachine.getSlots().size(); i++) {
                    String itemName = currentMachine.getSlots().get(i).getItem().getName();
                    while (true) {
                        String quantityInput = JOptionPane.showInputDialog(addItemFrame, "Please enter the quantity of " + itemName + ":");
                        try {
                            int quantity = Integer.parseInt(quantityInput);
                            if (quantity >= 0) {
                                if (currentMachine.setQuantity(itemName, quantity)) {
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(addItemFrame, "Invalid quantity. Please try again.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(addItemFrame, "Invalid quantity. Please enter a number greater than or equal to 0.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addItemFrame, "Invalid input. Please enter a valid integer.");
                        }
                    }
                }

                addItemFrame.dispose();
            }
        });

        addItemFrame.add(addItemButton);
        addItemFrame.add(finishButton);
        addItemFrame.setVisible(true);
    }


    public void testFeatures() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Vending Machine");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new GridLayout(2, 1));

            JButton purchaseItemButton = new JButton("Purchase an Item");
            JButton purchaseSmoothieButton = new JButton("Purchase a Customizable Smoothie");

            purchaseItemButton.addActionListener(e -> purchaseItemAction());
            purchaseSmoothieButton.addActionListener(e -> purchaseSmoothieAction());

            frame.add(purchaseItemButton);
            frame.add(purchaseSmoothieButton);
            frame.setVisible(true);
        });
    }

    private void purchaseItemAction() {
        if (currentMachine != null) {
            ArrayList<Slot> slots = currentMachine.getSlots();
            if (slots.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No items available in the vending machine.");
                return;
            }

            String[] itemNames = slots.stream()
                    .filter(slot -> !slot.isEmpty())
                    .map(slot -> slot.getItem().getName() + " ($" + slot.getItem().getPrice() + ")")
                    .toArray(String[]::new);

            if (itemNames.length == 0) {
                JOptionPane.showMessageDialog(null, "No items available in the vending machine.");
                return;
            }

            String selectedItem = (String) JOptionPane.showInputDialog(null,
                    "Choose an item:", "Item Selection", JOptionPane.QUESTION_MESSAGE,
                    null, itemNames, itemNames[0]);

            if (selectedItem != null) {
                Item chosenItem = slots.stream()
                        .filter(slot -> !slot.isEmpty())
                        .map(Slot::getItem)
                        .filter(item -> selectedItem.equals(item.getName() + " ($" + item.getPrice() + ")"))
                        .findFirst()
                        .orElse(null);

                if (chosenItem != null) {
                    double price = chosenItem.getPrice();
                    double payment = 0;

                    // Keep prompting for payment until a valid input is provided
                    while (payment < price) {
                        String paymentInput = JOptionPane.showInputDialog(null, "Cost of " + chosenItem.getName() + ": $" + price
                                + "\nEnter your payment amount:");

                        // If the user presses "Cancel" or closes the dialog, return
                        if (paymentInput == null) {
                            return;
                        }

                        try {
                            payment = Double.parseDouble(paymentInput);
                            if (payment < price) {
                                JOptionPane.showMessageDialog(null, "Insufficient funds. Please enter a payment amount greater than or equal to $" + price + ":");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid payment amount.");
                        }
                    }

                    // Calculate change
                    double change = payment - price;
                    ArrayList<Integer> changeList = currentMachine.getChange((int) change);
                    ArrayList<Integer> denominations = currentMachine.getMoneyBox().getAvailableDenominations();
                    int totalChange = 0;
                    StringBuilder changeDetails = new StringBuilder();
                    changeDetails.append("Your change is: \n");

                    for (int i = 0; i < changeList.size(); i++) {
                        if (changeList.get(i) != 0) {
                            changeDetails.append(changeList.get(i)).append(" ").append(denominations.get(i)).append(" peso bills\n");
                            totalChange += changeList.get(i) * denominations.get(i);
                        }
                    }

                    if (totalChange == change) {
                        JOptionPane.showMessageDialog(null, changeDetails.toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Error in dispensing change. Please try again.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No vending machine has been created yet.");
        }
    }
    private void purchaseSmoothieAction() {
        if (currentMachine != null) {
            ArrayList<Smoothie> smoothies = currentMachine.getCustomizableProducts();
            if (smoothies.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No customizable smoothies available.");
                return;
            }

            String[] smoothieNames = smoothies.stream()
                    .map(smoothie -> smoothie.getName() + " ($" + smoothie.getPrice() + ")")
                    .toArray(String[]::new);

            String selectedSmoothie = (String) JOptionPane.showInputDialog(null,
                    "Choose a smoothie:", "Smoothie Selection", JOptionPane.QUESTION_MESSAGE,
                    null, smoothieNames, smoothieNames[0]);

            if (selectedSmoothie != null) {
                Smoothie chosenSmoothie = smoothies.stream()
                        .filter(smoothie -> selectedSmoothie.equals(smoothie.getName() + " ($" + smoothie.getPrice() + ")"))
                        .findFirst()
                        .orElse(null);

                if (chosenSmoothie != null) {
                    ArrayList<Fruit> ingredients = currentMachine.getCustomizableIngredients();
                    if (ingredients.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No customizable ingredients available.");
                        return;
                    }

                    String[] ingredientNames = ingredients.stream()
                            .map(ingredient -> ingredient.getName() + " ($" + ingredient.getPrice() + ")")
                            .toArray(String[]::new);

                    ArrayList<Fruit> chosenIngredients = new ArrayList<>();
                    while (true) {
                        String selectedIngredient = (String) JOptionPane.showInputDialog(null,
                                "Choose an ingredient (Press Cancel to finish selecting):", "Ingredient Selection",
                                JOptionPane.QUESTION_MESSAGE, null, ingredientNames, ingredientNames[0]);

                        if (selectedIngredient == null) {
                            break;
                        }

                        Fruit chosenFruit = ingredients.stream()
                                .filter(fruit -> selectedIngredient.equals(fruit.getName() + " ($" + fruit.getPrice() + ")"))
                                .findFirst()
                                .orElse(null);

                        if (chosenFruit != null) {
                            chosenIngredients.add(chosenFruit);
                        }
                    }

                    if (!chosenIngredients.isEmpty()) {
                        // Calculate the total price of the customizable smoothie
                        double totalPrice = chosenSmoothie.getPrice();
                        for (Fruit ingredient : chosenIngredients) {
                            totalPrice += ingredient.getPrice();
                        }

                        // Show smoothie details and total price to the user
                        StringBuilder smoothieDetails = new StringBuilder();
                        smoothieDetails.append("Smoothie: ").append(chosenSmoothie.getName()).append("\n");
                        smoothieDetails.append("Ingredients: ");
                        for (Fruit ingredient : chosenIngredients) {
                            smoothieDetails.append(ingredient.getName()).append(", ");
                        }
                        smoothieDetails.delete(smoothieDetails.length() - 2, smoothieDetails.length()); // Remove last comma
                        smoothieDetails.append("\nTotal Price: $").append(totalPrice);

                        JOptionPane.showMessageDialog(null, smoothieDetails.toString());

                        // Prompt for payment
                        while (true) {
                            String paymentInput = JOptionPane.showInputDialog(null, "Enter your payment amount, total price is $" + totalPrice +":");
                            try {
                                int payment = Integer.parseInt(paymentInput);
                                if (payment < totalPrice) {
                                    JOptionPane.showMessageDialog(null, "Insufficient payment. Please enter a higher amount.");
                                } else {
                                    // Calculate change
                                    int change = payment - (int) totalPrice;
                                    ArrayList<Integer> changeList = currentMachine.getChange(change);
                                    ArrayList<Integer> denominations = currentMachine.getMoneyBox().getAvailableDenominations();
                                    int totalChange = 0;
                                    for (int i = 0; i < changeList.size(); i++) {
                                        totalChange += changeList.get(i) * denominations.get(i);
                                    }
                                    if (totalChange == change) {
                                        StringBuilder changeDetails = new StringBuilder();
                                        changeDetails.append("Your change is: \n");
                                        for (int i = 0; i < changeList.size(); i++) {
                                            if (changeList.get(i) != 0) {
                                                changeDetails.append(changeList.get(i)).append(" ").append(denominations.get(i)).append(" peso bills\n");
                                                break;
                                            }
                                        }
                                        JOptionPane.showMessageDialog(null, changeDetails.toString());
                                    }
                                    break;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid payment amount.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No valid ingredients chosen. Purchase canceled.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No vending machine has been created yet.");
        }
    }

    public void maintenance() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Maintenance Mode");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLayout(new GridLayout(6, 1));

                JButton restockItemsButton = new JButton("Restock Items");
                JButton setPriceButton = new JButton("Set Price of Item");
                JButton collectPaymentButton = new JButton("Collect Payment / Money");
                JButton replenishDenominationButton = new JButton("Replenish Denominations");
                JButton transactionSummaryButton = new JButton("Print Summary of Transactions");
                JButton exitButton = new JButton("Exit Maintenance");

                restockItemsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            ArrayList<Item> items = currentMachine.displayItems();
                            if (items.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "No items available in the vending machine.");
                            } else {
                                String[] itemNames = new String[items.size()];
                                for (int i = 0; i < items.size(); i++) {
                                    itemNames[i] = items.get(i).getName();
                                }

                                String selectedItem = (String) JOptionPane.showInputDialog(null,
                                        "Choose an item to restock:", "Item Selection", JOptionPane.QUESTION_MESSAGE,
                                        null, itemNames, itemNames[0]);

                                if (selectedItem != null) {
                                    for (Item item : items) {
                                        if (selectedItem.equals(item.getName())) {
                                            String quantityInput = JOptionPane.showInputDialog("Enter the quantity to restock:");
                                            try {
                                                int quantity = Integer.parseInt(quantityInput);
                                                if (quantity >= 0) {
                                                    currentMachine.restockItem(selectedItem, quantity);
                                                    JOptionPane.showMessageDialog(frame, "Restock successful!");
                                                } else {
                                                    JOptionPane.showMessageDialog(frame, "Invalid quantity. Please enter a number greater than or equal to 0.");
                                                }
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer.");
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });

                setPriceButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            ArrayList<Item> items = currentMachine.displayItems();
                            if (items.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "No items available in the vending machine.");
                            } else {
                                String[] itemNames = new String[items.size()];
                                for (int i = 0; i < items.size(); i++) {
                                    itemNames[i] = items.get(i).getName();
                                }

                                String selectedItem = (String) JOptionPane.showInputDialog(null,
                                        "Choose an item to set the price:", "Item Selection", JOptionPane.QUESTION_MESSAGE,
                                        null, itemNames, itemNames[0]);

                                if (selectedItem != null) {
                                    for (Item item : items) {
                                        if (selectedItem.equals(item.getName())) {
                                            String priceInput = JOptionPane.showInputDialog("Enter the new price for " + selectedItem + ":");
                                            try {
                                                double price = Double.parseDouble(priceInput);
                                                if (price >= 0) {
                                                    currentMachine.setItemPrice(selectedItem, price);
                                                    JOptionPane.showMessageDialog(frame, "Price set successfully!");
                                                } else {
                                                    JOptionPane.showMessageDialog(frame, "Invalid price. Please enter a price greater than or equal to 0.");
                                                }
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });

                collectPaymentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            String amountInput = JOptionPane.showInputDialog("Enter the amount of money you would like to collect:");
                            try {
                                int amount = Integer.parseInt(amountInput);
                                if (amount >= 0) {
                                    currentMachine.collectMoney(amount);
                                    JOptionPane.showMessageDialog(frame, "Collection successful!");
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter an amount greater than or equal to 0.");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });

                replenishDenominationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            ArrayList<Integer> availableDenominations = new ArrayList<>(currentMachine.getMoneyBox().getAvailableDenominations());
                            availableDenominations.remove(Integer.valueOf(1));

                            String[] denominationOptions = new String[availableDenominations.size()];
                            for (int i = 0; i < availableDenominations.size(); i++) {
                                denominationOptions[i] = availableDenominations.get(i).toString();
                            }

                            String selectedDenomination = (String) JOptionPane.showInputDialog(null,
                                    "Choose a denomination to replenish:", "Denomination Selection", JOptionPane.QUESTION_MESSAGE,
                                    null, denominationOptions, denominationOptions[0]);

                            if (selectedDenomination != null) {
                                int denomination = Integer.parseInt(selectedDenomination);
                                String quantityInput = JOptionPane.showInputDialog("Enter the quantity to replenish for " + denomination + ":");
                                try {
                                    int quantity = Integer.parseInt(quantityInput);
                                    if (quantity >= 0) {
                                        currentMachine.replenishDenomination(denomination, quantity);
                                        JOptionPane.showMessageDialog(frame, "Replenishment successful!");
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Invalid quantity. Please enter a number greater than or equal to 0.");
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer.");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });

                transactionSummaryButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentMachine != null) {
                            String transactionSummary = currentMachine.displayTransactionSummary();
                            JTextArea textArea = new JTextArea(transactionSummary);
                            textArea.setEditable(false);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(400, 200));
                            JOptionPane.showMessageDialog(frame, scrollPane, "Transaction Summary", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "No vending machine has been created yet.");
                        }
                    }
                });

                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                    }
                });

                frame.add(restockItemsButton);
                frame.add(setPriceButton);
                frame.add(collectPaymentButton);
                frame.add(replenishDenominationButton);
                frame.add(transactionSummaryButton);
                frame.add(exitButton);
                frame.setVisible(true);
            }
        });
    }
}