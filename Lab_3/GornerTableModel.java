import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {

    /**
          Coefficients of polynomial */
    private Double[] mCoefficients;

    /**
         Start and end values of interval */
    private Double mFrom;
    private Double mTo;

    /**
         Tabulation step */
    private Double mStep;


    public GornerTableModel(Double[] coefficients, Double from, Double to, Double step) {
        this.mCoefficients = coefficients;
        this.mFrom = from;
        this.mTo = to;
        this.mStep = step;
    }

    public int getColumnCount() {
        return 4;
    }

    public Class <?> getColumnClass(int column) {
        switch (column) {
            case 2:
                return Float.class;
            default:
                return Double.class;
        }
    }

    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "X";
        case 1:
            return "Y(double)";
        case 2:
            return "Y(float)";
        default:
            return "Y(double) - Y(float)";
        }
    }

    public int getRowCount() {
        return new Double(Math.ceil((mTo - mFrom) / mStep)).intValue() + 1;
    }

    public Object getValueAt(int row, int column) {
        double x = mFrom + mStep * row;
        if (column == 0) {
            return x;
        } else if (column == 1) {
            Double result = mCoefficients[0];
            for (int i = 1; i < mCoefficients.length; ++i) {
                result = result * x + mCoefficients[i];
            }
            return result;
        } else if (column == 2) {
            Float result = mCoefficients[0].floatValue();
            for (int i = 1; i < mCoefficients.length; ++i) {
                result = result * (float)x + mCoefficients[i].floatValue();
            }
            return result;
        } else {
            Double resultDouble = mCoefficients[0];
            for (int i = 1; i < mCoefficients.length; ++i) {
                resultDouble = resultDouble * x + mCoefficients[i];
            }
            Float resultFloat = mCoefficients[0].floatValue();
            for (int i = 1; i < mCoefficients.length; ++i) {
                resultFloat = resultFloat * (float)x + mCoefficients[i].floatValue();
            }
            
            return resultDouble  - (double)resultFloat;
        }
    }

    public Double getFrom() {
        return mFrom;
    }

    public Double getTo() {
        return mTo;
    }

    public Double getStep() {
        return mStep;
    }
}
