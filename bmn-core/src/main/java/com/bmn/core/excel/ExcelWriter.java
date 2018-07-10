
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.core.excel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @date 2018-05-10
 * @author
 */
public class ExcelWriter {

    private static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);

    private HSSFWorkbook workbook;

    public ExcelWriter() {
        this.workbook = new HSSFWorkbook();
    }


    private HSSFSheet sheet;

    public ExcelWriter createSheet(String name) {
        if (this.workbook == null) {
            throw new NullPointerException("workbook is null");
        }
        this.sheet = this.workbook.createSheet(name);

        this.row = null;

        return this;
    }

    private HSSFRow row;

    public ExcelWriter createRow(int rowIndex) {
        if (this.sheet == null) {
            throw new NullPointerException("create row, but sheet is null");
        }
        row = sheet.createRow(rowIndex);

        this.rowIndex = rowIndex;

        this.cellIndex = 0;
        return this;
    }

    // 当前行
    private int rowIndex;

    // 当前列
    private int cellIndex;

    public ExcelWriter addCellValue(String value) {
        if (row == null) {
            logger.warn("add cell: {} value: {} fail, no row: {} had created", this.cellIndex,
                    value, rowIndex);
            return this;
        }

        row.createCell(cellIndex).setCellValue(value);

        cellIndex++;

        return this;
    }

    public ExcelWriter addCellValue(int value) {
        if (row == null) {
            logger.warn("add cell: {} value: {} fail, no row: {} had created", this.cellIndex,
                    value, rowIndex);
            return this;
        }

        row.createCell(cellIndex).setCellValue(value);

        cellIndex++;

        return this;
    }

    public void write(OutputStream os) throws IOException {
        this.workbook.write(os);
    }

    public ExcelWriter build() {
        this.sheet = null;
        this.row = null;
        return this;
    }

}
