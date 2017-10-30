
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRendererFlag implements TableCellRenderer {

    private String mNeedle = null;

    private JPanel mPanel = new JPanel();
    private JLabel mLabel = new JLabel();

    /**
         For formatting string representations of numbers */
    private DecimalFormat mFormatter = (DecimalFormat)NumberFormat.getInstance();


    public GornerTableCellRendererFlag() {
        formatterSettings(5, '.');
        mPanel.add(mLabel);
        mPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedNumber = mFormatter.format(value);
        if (col == 2 && mNeedle != null && mNeedle.equals(formattedNumber)) {
        	
            mLabel.removeAll();
            mLabel.setText("\u2611");
    
        } else {
            mLabel.setText(formattedNumber);
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
