package br.jorge.codebind;

import com.joptimizer.exception.JOptimizerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public App() {
        prepareGUI();
    }

    public static void main(String[] args) {
        App app = new App();
        app.showEvent();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("App");
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

        buttonClear.addActionListener(new ButtonClickListener());
        buttonSubmit.addActionListener(new ButtonClickListener());

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

    private void tryOptimize(){
        try {
            if (textFieldMinFat.getText().trim().isEmpty()) {
                resLabel.setForeground(Color.red);
                resLabel.setText("Preencha todos os campos");
            } else {
                Diet diet = new Diet(Double.valueOf(textFieldMinFat.getText()), Double.valueOf(textFieldMinCarbs.getText()), Double.valueOf(textFieldMinProtein.getText()), Double.valueOf(textFieldMaxPortions.getText()));
                Product pA = new Product(Double.valueOf(textFieldCostA.getText()), Double.valueOf(textFieldFatA.getText()), Double.valueOf(textFieldCarbsA.getText()), Double.valueOf(textFieldProteinA.getText()));
                Product pB = new Product(Double.valueOf(textFieldCostB.getText()), Double.valueOf(textFieldFatB.getText()), Double.valueOf(textFieldCarbsB.getText()), Double.valueOf(textFieldProteinB.getText()));

                resLabel.setForeground(Color.black);
                resLabel.setText("Otimizando...");

                OptimizedDiet optimizedDiet = Solver.doMath(diet, pA, pB);

                textFieldSolA.setText(optimizedDiet.amountOfPorduct1 + "");
                textFieldSolB.setText(optimizedDiet.amountOfPorduct2 + "");
                textFieldSolFunction.setText(optimizedDiet.totalCost + "");

                resLabel.setText("Otimização completa");
            }
        } catch (NumberFormatException ex) {
            resLabel.setForeground(Color.red);
            resLabel.setText("Preencha os campos corretamente. Utilize \".\" como separador decimal. Não utilize vírgula.");
        } catch (JOptimizerException e) {
            resLabel.setForeground(Color.red);
            resLabel.setText("Ocorreu um erro ao otimizar a função.");
        }
    }

    private class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Clear")) {
                clearAll();
            } else if (command.equals("Submit")) {
                tryOptimize();
            }
        }
    }
}
