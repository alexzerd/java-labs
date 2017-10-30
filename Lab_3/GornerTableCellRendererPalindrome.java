import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.lang.StringBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRendererPalindrome implements TableCellRenderer {

    private String mNeedle = null;

    private JPanel mPanel = new JPanel();
    private JLabel mLabel = new JLabel();

    /**
         For formatting string representations of numbers */
    private DecimalFormat mFormatter = (DecimalFormat)NumberFormat.getInstance();


    public GornerTableCellRendererPalindrome() {
        formatterSettings(5, '.');
        mPanel.add(mLabel);
        mPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedNumber = mFormatter.format(value);
        mLabel.setText(formattedNumber);
        if (mNeedle != null && formattedNumber.equals(new StringBuffer(formattedNumber).reverse().toString())) {
            mPanel.setBackground(Color.RED);
        } else {
            mPanel.setBackground(Color.WHITE);
        }
        return mPanel;
    }

    public void setNeedle(String needle) {
        this.mNeedle = needle;
    }

    /**
         Method configure formatter
         First argument(type 'int') set number of digits to display
         Second argument(type'char') set separator between integer and fractional part */
    private void formatterSettings(int digits, char separator) {
        mFormatter.setMaximumFractionDigits(digits);
        mFormatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedNumber = mFormatter.getDecimalFormatSymbols();
        dottedNumber.setDecimalSeparator(separator);
        mFormatter.setDecimalFormatSymbols(dottedNumber);
    }
}