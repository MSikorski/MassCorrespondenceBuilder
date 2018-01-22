package org.mateuszsikorski.masscorrespondencebuilder.correspondence;

public class Format {

	private final String paperFormat;
	private final int verticalMargin;
	private final int horizontalMargin;
	
	public Format(String paperFormat, int verticalMargin, int horizontalMargin) {
		this.paperFormat = paperFormat;
		this.verticalMargin = verticalMargin;
		this.horizontalMargin = horizontalMargin;
	}

	public String getPaperFormat() {
		return paperFormat;
	}

	public int getVerticalMargin() {
		return verticalMargin;
	}

	public int getHorizontalMargin() {
		return horizontalMargin;
	}

	@Override
	public String toString() {
		return "Format [paperFormat=" + paperFormat + ", verticalMargin=" + verticalMargin + ", horizontalMargin="
				+ horizontalMargin + "]";
	}
	
}
