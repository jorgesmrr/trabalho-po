package br.jorge.codebind;

import com.joptimizer.exception.JOptimizerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class App {

    private JFrame mainFrame;
    private JPanel panelMain;
    private JButton buttonClear;
    private JButton buttonSubmit;
    private JTextField textFieldMinFat;
    private JTextField textFieldMinProtein;
    private JTextField textFieldMinCarbs;
    private JTextField textFieldMaxPortions;
    private JTextField textFieldProteinA;
    private JTextField textFieldCostA;
    private JTextField textFieldFatA;
    private JTextField textFieldCarbsA;
    private JTextField textFieldFatB;
    private JTextField textFieldProteinB;
    private JTextField textFieldCostB;
    private JTextField textFieldCarbsB;
    private JTextField textFieldSolFunction;
    private JTextField textFieldSolA;
    private JTextField textFieldSolB;
    private JLabel resLabel;
    private JButton buttonSample;
    private JCheckBox checkboxFormat;

    private App() {
        prepareGUI();
    }

    public static void main(String[] args) {
        App app = new App();
        app.showEvent();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Otimizador de dieta");
        mainFrame.setContentPane(panelMain);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        textFieldMinFat.setColumns(8);
        textFieldMinProtein.setColumns(8);
        textFieldMinCarbs.setColumns(8);
        textFieldMaxPortions.setColumns(8);
        textFieldProteinA.setColumns(8);
        textFieldCostA.setColumns(8);
        textFieldFatA.setColumns(8);
        textFieldCarbsA.setColumns(8);
        textFieldFatB.setColumns(8);
        textFieldProteinB.setColumns(8);
        textFieldCostB.setColumns(8);
        textFieldCarbsB.setColumns(8);
        textFieldSolFunction.setColumns(8);
        textFieldSolA.setColumns(8);
        textFieldSolB.setColumns(8);

        buttonClear.setActionCommand("Clear");
        buttonSubmit.setActionCommand("Submit");
        buttonSample.setActionCommand("Sample");

        buttonClear.addActionListener(new ButtonClickListener());
        buttonSubmit.addActionListener(new ButtonClickListener());
        buttonSample.addActionListener(new ButtonClickListener());

        resLabel.setText("Aguardando");
    }

    private void showEvent() {
        mainFrame.setVisible(true);
    }

    private void clearAll(){
        textFieldMinFat.setText("");
        textFieldMinProtein.setText("");
        textFieldMinCarbs.setText("");
        textFieldMaxPortions.setText("");
        textFieldProteinA.setText("");
        textFieldCostA.setText("");
        textFieldFatA.setText("");
        textFieldCarbsA.setText("");
        textFieldFatB.setText("");
        textFieldProteinB.setText("");
        textFieldCostB.setText("");
        textFieldCarbsB.setText("");
        textFieldSolFunction.setText("");
        textFieldSolA.setText("");
        textFieldSolB.setText("");

        resLabel.setText("Aguardando");
    }

    private void putSample(){
        textFieldMinFat.setText("24");
        textFieldMinProtein.setText("4");
        textFieldMinCarbs.setText("36");
        textFieldMaxPortions.setText("5");

        textFieldProteinA.setText("2");
        textFieldCostA.setText("0,2");
        textFieldFatA.setText("8");
        textFieldCarbsA.setText("12");

        textFieldFatB.setText("12");
        textFieldProteinB.setText("1");
        textFieldCostB.setText("0,3");
        textFieldCarbsB.setText("12");
    }

    private String formatNumber(double number) {
        if (checkboxFormat.isSelected()){
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(number);
        } else{
            return number + "";
        }
    }

    /**
     * Obtém os valores do formulário, constroi os modelos do problema, envia para o resolvedor e mostra a solução na interface de usuário.
     */
    private void tryOptimize(){
        try {
            if (textFieldMinFat.getText().trim().isEmpty()
                    || textFieldMinProtein.getText().trim().isEmpty()
                    || textFieldMinCarbs.getText().trim().isEmpty()
                    || textFieldMaxPortions.getText().trim().isEmpty()
                    || textFieldProteinA.getText().trim().isEmpty()
                    || textFieldCostA.getText().trim().isEmpty()
                    || textFieldFatA.getText().trim().isEmpty()
                    || textFieldCarbsA.getText().trim().isEmpty()
                    || textFieldFatB.getText().trim().isEmpty()
                    || textFieldProteinB.getText().trim().isEmpty()
                    || textFieldCostB.getText().trim().isEmpty()
                    || textFieldCarbsB.getText().trim().isEmpty()) {
                resLabel.setForeground(Color.red);
                resLabel.setText("Preencha todos os campos");
            } else {
                Diet diet = new Diet(
                        Double.valueOf(textFieldMinFat.getText().replace(",", ".")),
                        Double.valueOf(textFieldMinCarbs.getText().replace(",", ".")),
                        Double.valueOf(textFieldMinProtein.getText().replace(",", ".")),
                        Double.valueOf(textFieldMaxPortions.getText().replace(",", ".")));
                Product pA = new Product(
                        Double.valueOf(textFieldCostA.getText().replace(",", ".")),
                        Double.valueOf(textFieldFatA.getText()),
                        Double.valueOf(textFieldCarbsA.getText()),
                        Double.valueOf(textFieldProteinA.getText()));
                Product pB = new Product(
                        Double.valueOf(textFieldCostB.getText().replace(",", ".")),
                        Double.valueOf(textFieldFatB.getText().replace(",", ".")),
                        Double.valueOf(textFieldCarbsB.getText().replace(",", ".")),
                        Double.valueOf(textFieldProteinB.getText().replace(",", ".")));

                resLabel.setForeground(Color.black);
                resLabel.setText("Otimizando...");

                OptimizedDiet optimizedDiet = Solver.doMath(diet, pA, pB);

                textFieldSolA.setText(formatNumber(optimizedDiet.amountOfPorduct1));
                textFieldSolB.setText(formatNumber(optimizedDiet.amountOfPorduct2));
                textFieldSolFunction.setText(formatNumber(optimizedDiet.totalCost));

                resLabel.setText("Otimização completa");
            }
        } catch (NumberFormatException ex) {
            resLabel.setForeground(Color.red);
            resLabel.setText("Preencha os campos corretamente.");
        } catch (JOptimizerException e) {
            resLabel.setForeground(Color.red);
            resLabel.setText("Não foi possível calcular a solução ótima.");
        }
    }

    private class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "Clear":
                    clearAll();
                    break;
                case "Submit":
                    tryOptimize();
                    break;
                case "Sample":
                    putSample();
                    break;
            }
        }
    }
}
