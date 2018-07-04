
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.core.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 
 *
 * @date 2018-05-10
 * @author
 */
public class ExcelReader {

    private Workbook workbook;

    public ExcelReader(InputStream is) throws Exception {
        this.workbook = WorkbookFactory.create(is);
    }

    private Sheet sheet;

    public ExcelReader readSheet(int index) {
        if (this.workbook == null) {
            throw new NullPointerException("workbook is null");
        }
        this.sheet = this.workbook.getSheetAt(index);

        this.row = null;

        return this;
    }

    private Row row;

    public ExcelReader readRow(int rowIndex) {
        if (this.sheet == null) {
            throw new NullPointerException("create row, but sheet is null");
        }
        row = sheet.getRow(rowIndex);

        this.rowIndex = rowIndex;

        this.cellIndex = 0;
        return this;
    }

    // 当前行
    private int rowIndex;

    // 当前列
    private int cellIndex;

    public String getCellStringValue() {
        if (row == null) {
            return "";
        }

        String v = row.getCell(cellIndex).getStringCellValue();

        cellIndex++;

        return v.trim();
    }

    public int getCellIntValue() {
        return (int) getCellDoubleValue();
    }

    public double getCellDoubleValue() {
        if (row == null) {
            return 0;
        }

        double v = row.getCell(cellIndex).getNumericCellValue();

        cellIndex++;

        return v;
    }

    public void write(OutputStream os) throws IOException {
        this.workbook.write(os);
    }


}
