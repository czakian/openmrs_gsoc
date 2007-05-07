package org.openmrs.util;

/**
 * Represents a bounded or unbounded numeric range.
 * By default the range is closed (ake inclusive) on the low end and open (aka exclusive) on the high end: mathematically "[low, high)".
 * (I'm not using the similarly-named class from Apache commons because it doesn't implement comparable, and because it only allows inclusive bounds.)
 * @author djazayeri
 */
public class DoubleRange implements Comparable<DoubleRange> {

	private Double low;
	private Double high;
	private boolean closedLow = true; //TODO: add setters and getters for these
	private boolean closedHigh = false;
	
	public DoubleRange() { }
	
	public DoubleRange(Double low, Double high) {
		this.low = low == null ? new Double(Double.NEGATIVE_INFINITY) : low;
		this.high = high == null ? new Double(Double.POSITIVE_INFINITY) : high;
	}

	/**
	 * @return Returns the high.
	 */
	public Double getHigh() {
		return high;
	}

	/**
	 * @param high The high to set.
	 */
	public void setHigh(Double high) {
		this.high = high == null ? new Double(Double.POSITIVE_INFINITY) : high;
	}

	/**
	 * @return Returns the low.
	 */
	public Double getLow() {
		return low;
	}

	/**
	 * @param low The low to set.
	 */
	public void setLow(Double low) {
		this.low = low == null ? new Double(Double.NEGATIVE_INFINITY) : low;
	}
	
	/**
	 * first sorts according to low-bound (ascending) then according to high-bound (descending)
	 */
 	public int compareTo(DoubleRange other) {
		int temp = low.compareTo(other.low);
		if (temp == 0) {
			temp = other.high.compareTo(high); 
		}
		return temp;
 	}
	
	public boolean contains(double d) {
		if (low != null) {
			if (closedLow) {
				if (d < low) {
					return false;
				}
			} else {
				if (d <= low) {
					return false;
				}
			}
		}
		if (high != null) {
			if (closedHigh) {
				if (d > high) {
					return false;
				}
			} else {
				if (d >= high) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer();
		if (low != null && low.doubleValue() != Double.NEGATIVE_INFINITY) {
			ret.append(">");
			if (closedLow) {
				ret.append("=");
			}
			ret.append(" " + Format.format(low));
			if (high != null && high.doubleValue() != Double.NEGATIVE_INFINITY) {
				ret.append(" and ");
			}
		}
		if (high != null && high.doubleValue() != Double.POSITIVE_INFINITY) {
			ret.append("<");
			if (closedHigh) {
				ret.append("=");
			}
			ret.append(" " + Format.format(high));
		}
		return ret.toString();
	}

	public boolean equals(Object o) {
		DoubleRange other = (DoubleRange) o;
		return low.equals(other.low) && high.equals(other.high);
	}
	
}
