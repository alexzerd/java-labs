
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;
    private JTextField textFieldResult;
    private JTextField textFieldMem1;
    private JTextField textFieldMem2;
    private JTextField textFieldMem3;
    private JTextField textFieldActive;
    private double mem1 = 0.0;
    private double mem2 = 0.0;
    private double mem3 = 0.0;
    private double activeVar = 0.0;

    private int formulaId = 1;
    private String picFile = "src\\Pics\\formula1.png";
    private ImageIcon formulaPic = new ImageIcon(picFile);
    private JLabel labelForPics = new JLabel(formulaPic);


    
    
    public double calculate1(double x, double y, double z){
    	
        return Math.sin((Math.log(y)+Math.sin((Math.PI*Math.pow(y,2))*Math.pow(Math.pow(x,2)+
                     (Math.sin(z)+ Math.exp(Math.cos(z))),1/4))));
        
    }
    
    public double calculate2(double x, double y, double z){
    	
        return Math.pow(Math.cos(Math.exp(x))+Math.log((1+y)*(1+y))+
                Math.pow(Math.exp(Math.cos(x))+Math.sin(Math.PI*z)*Math.sin(Math.PI*z),1/2)+
                Math.pow(1/x,1/2)+Math.cos(y*y),Math.sin(z));
        
    }

    
    ButtonGroup radioButtons = new ButtonGroup();
    
    Box hBoxFormulaType = Box.createHorizontalBox();
    
    private void addRadioButton(String buttonName, final int formulaId){
    	
        JRadioButton button = new JRadioButton(buttonName);
        if (formulaId == 1){
            button.setSelected(true);
        }
        
        button.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.formulaId = formulaId;
                if (formulaId == 1){
                    picFile = "src\\Pics\\formula1.png";
                }
                else if (formulaId == 2){
                    picFile = "src\\Pics\\formula2.png";
                }
                formulaPic = new ImageIcon(picFile);
                labelForPics.setIcon(formulaPic);


            }
        });
        
        radioButtons.add(button);
        hBoxFormulaType.add(button);
        
    }
    
    public MainFrame(){
    	
        super("Вычисление формулы, Вариант С");
        
        setSize(WIDTH,HEIGHT);
        
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        setLocation((kit.getScreenSize().width - WIDTH)/2,
        		
                (kit.getScreenSize().height - HEIGHT)/2);
        

        //Область выбора формулы

        hBoxFormulaType.add(Box.createHorizontalGlue()); 
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        hBoxFormulaType.add(Box.createHorizontalGlue());
        hBoxFormulaType.setBorder(BorderFactory.createLineBorder(Color.black));

        
        Box hBoxFormulaPics = Box.createHorizontalBox();
        hBoxFormulaPics.add(labelForPics); 


        //Область ввода значений переменных
        
        JLabel labelForX = new JLabel("X"); 
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize((textFieldX.getPreferredSize()));
        JLabel labelForY = new JLabel("Y");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize((textFieldY.getPreferredSize()));
        JLabel labelForZ = new JLabel("Z", 10);
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize((textFieldZ.getPreferredSize()));

        //Коробка под переменные
        
        Box hBoxVariables = Box.createHorizontalBox();
        hBoxVariables.setBorder(BorderFactory.createLineBorder(Color.black));
        hBoxVariables.add(Box.createHorizontalGlue());
        hBoxVariables.add(labelForX);
        hBoxVariables.add(Box.createHorizontalStrut(10));
        hBoxVariables.add(textFieldX);
        hBoxVariables.add(Box.createHorizontalStrut(100));
        hBoxVariables.add(labelForY);
        hBoxVariables.add(Box.createHorizontalStrut(10));
        hBoxVariables.add(textFieldY);
        hBoxVariables.add(Box.createHorizontalStrut(100));
        hBoxVariables.add(labelForZ);
        hBoxVariables.add(Box.createHorizontalStrut(10));
        hBoxVariables.add(textFieldZ);
        hBoxVariables.add(Box.createHorizontalGlue()); 

      //Добавление поля Результат
        
        Box hBoxResult = Box.createHorizontalBox(); 
        
        JLabel resultLabel = new JLabel("Результат: ");
        hBoxResult.add(resultLabel);
        textFieldResult = new JTextField("0", 30);
        textFieldResult.setMaximumSize(textFieldResult.getPreferredSize());
        hBoxResult.add(textFieldResult);
        hBoxResult.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Добавление кнопки Вычислить

        JButton buttonCalculate = new JButton("Вычислить");
        buttonCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double x = Double.parseDouble(textFieldX.getText());
                    double y = Double.parseDouble(textFieldY.getText());
                    double z = Double.parseDouble(textFieldZ.getText());
                    double result = 0.0;
                    if (formulaId == 1){
                        result = calculate1(x,y,z);
                    }
                    else if (formulaId == 2){
                        result = calculate2(x, y, z);
                    }
                    textFieldResult.setText(String.valueOf(result));
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        
        //Добавление кнопки Сброс

        JButton buttonReset = new JButton("Сброс");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }
        });
        
       //Коробка под кнопки
        
        Box hBoxButtons = Box.createHorizontalBox();
        hBoxButtons.setBorder(BorderFactory.createLineBorder(Color.black));
        hBoxButtons.add(buttonCalculate);
        hBoxButtons.add(buttonReset); 
        
        //Коробка под память

        Box hBoxMemory = Box.createHorizontalBox();
        hBoxMemory.add(Box.createHorizontalGlue());
        hBoxMemory.setBorder(BorderFactory.createLineBorder(Color.black));
        ButtonGroup memoryRadios = new ButtonGroup();
        
        //Добавление полей переменных памяти

        textFieldMem1 = new JTextField("0", 10);
        textFieldMem1.setMaximumSize((textFieldMem1.getPreferredSize()));
        textFieldMem1.setEditable(false);
        textFieldMem2 = new JTextField("0", 10);
        textFieldMem2.setMaximumSize((textFieldMem2.getPreferredSize()));
        textFieldMem2.setEditable(false);
        textFieldMem3 = new JTextField("0", 10);
        textFieldMem3.setMaximumSize((textFieldMem3.getPreferredSize()));
        textFieldMem3.setEditable(false);

        JRadioButton radioMem1 = new JRadioButton("Переменная 1");
        radioMem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mem1 = Double.parseDouble(textFieldMem2.getText());
                activeVar = mem1;
                textFieldActive = textFieldMem1;
            }
        });
        memoryRadios.add(radioMem1);
        hBoxMemory.add(radioMem1);
        hBoxMemory.add(textFieldMem1);

        JRadioButton radioMem2 = new JRadioButton("Переменная 2");
        radioMem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mem2 = Double.parseDouble(textFieldMem2.getText());
                activeVar = mem2;
                textFieldActive = textFieldMem2;
            }
        });
        memoryRadios.add(radioMem2);
        hBoxMemory.add(radioMem2);
        hBoxMemory.add(textFieldMem2);

        JRadioButton radioMem3 = new JRadioButton("Переменная 3");
        radioMem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mem3 = Double.parseDouble(textFieldMem3.getText());
                activeVar = mem3;
                textFieldActive = textFieldMem3;
            }
        });
        memoryRadios.add(radioMem3);
        hBoxMemory.add(radioMem3);
        hBoxMemory.add(textFieldMem3);
        
        

        //Добавление кнопки MC

        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    activeVar = 0.0;
                    textFieldActive.setText("0");
                } catch (NullPointerException ex){

                }

            }
        });
        
        
        hBoxMemory.add(buttonMC);
        
        //Добавление кнопки M+
        
        JButton buttonMPlus = new JButton("M+");
        buttonMPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    activeVar = activeVar + Double.parseDouble(textFieldResult.getText());
                    textFieldActive.setText(String.valueOf(activeVar));
                }
                catch (NullPointerException ex){

                }
            }
        });
        
        hBoxMemory.add(buttonMPlus);
        hBoxMemory.add(Box.createVerticalGlue());

        
        
        //Сборка фрейма

        Box mainBox = Box.createVerticalBox();
        //mainBox.add(Box.createVerticalGlue());


        mainBox.add(hBoxFormulaPics);
        mainBox.add(hBoxFormulaType);
        mainBox.add(hBoxVariables);
        mainBox.add(hBoxResult);
        mainBox.add(hBoxButtons);
        mainBox.add(hBoxMemory);

        getContentPane().add(mainBox);

    }
}

