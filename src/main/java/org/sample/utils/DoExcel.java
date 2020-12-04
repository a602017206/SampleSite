package org.sample.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.poi.ss.usermodel.CellType.*;

public class DoExcel {
    public static Workbook getWorkBook(String fileInput) {
        Workbook wb = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileInput);
            wb = WorkbookFactory.create(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return wb;
    }

    public static Sheet getSheet(String fileInput, int sheetNum) {
        Workbook wb = getWorkBook(fileInput);
        Sheet sheet = wb.getSheetAt(sheetNum);

        return sheet;
    }

    public static Sheet getSheet(String fileInput, String sheetName) {
        Workbook wb = getWorkBook(fileInput);
        Sheet sheet = wb.getSheet(sheetName);
        return sheet;
    }

    public static Cell getCell(Sheet sheet, int indexRow, int indexCell) {
        Row row = getRow(sheet, indexRow);
        Cell cell = row.getCell(indexCell);
        if (cell == null) {
            cell = row.createCell(indexCell);
        }
        return cell;
    }

    public static Cell getCell2(Sheet sheet, int indexRow, int indexCell) {
        Row row = getRow(sheet, indexRow);
        Cell cell = row.getCell(indexCell);

        return cell;
    }

    public static Cell getCell(Sheet sheet, String POS) {
        int Col = getCol(POS);
        int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;

        Row row = getRow(sheet, Row);
        Cell cell = row.getCell(Col);
        if (cell == null) {
            cell = row.createCell(Col);
        }
        return cell;
    }

    public static Row getRow(Sheet sheet, int indexRow) {
        Row row = sheet.getRow(indexRow);
        if (row == null) {
            row = sheet.createRow(indexRow);
        }
        return row;
    }

    public static Cell getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        return cell;
    }

    public static int getCol(String Pos) {
        int Col = 0;
        String TempPos = StrCommon.GetStr(Pos);
        int len = TempPos.length();
        for (int j = 1; j <= len; j++) {
            Col = (TempPos.charAt(j - 1) - 'A' + 1)
                    * (int) Math.pow(26.0D, len - j) + Col;
        }
        Col--;
        return Col;
    }

    @SuppressWarnings("deprecation")
    public static String GetCellValue(Sheet sheet, String POS) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            if (cell.getCellType() == STRING) {
                return cell.getStringCellValue().trim();
            }
            if (cell.getCellType() == NUMERIC) {
                String temp = String.valueOf(cell.getNumericCellValue());
                return StrCommon.RemoveZero(temp);
            }
            if (cell.getCellType() == BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            }
            if (cell.getCellType() == FORMULA) {
                String temp = cell.getStringCellValue().trim();
                if (temp.equals("")) {
                    temp = String.valueOf(cell.getNumericCellValue());
                }
                return StrCommon.RemoveZero(temp);
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String GetCellValue(Sheet sheet, int indexRow, int indexCell) {
        try {
            Cell cell = getCell(sheet, indexRow, indexCell);
            if (cell.getCellTypeEnum().equals(STRING)) {
                return cell.getStringCellValue().trim();
            }
            if (cell.getCellTypeEnum().equals(NUMERIC)) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                    return sdf.format(DateUtil.getJavaDate(cell
                            .getNumericCellValue()));
                }
                DecimalFormat df = new DecimalFormat("##.######");

                String temp = df.format(cell.getNumericCellValue());
                return StrCommon.RemoveZero(temp);
            }
            if (cell.getCellTypeEnum().equals(BOOLEAN)) {
                return String.valueOf(cell.getBooleanCellValue());
            }
            if (cell.getCellTypeEnum().equals(FORMULA)) {
                return cell.getStringCellValue();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date GetDateCellValue(Sheet sheet, String POS) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            return cell.getDateCellValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void FillCell(Sheet sheet, int indexRow, int indexCell,
                                String str) {
        try {
            Cell cell = getCell(sheet, indexRow, indexCell);
            if (str.compareTo("") == 0) {
                return;
            }
            if (cell != null) {
                cell.setCellValue(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void FillCell2(Sheet sheet, int indexRow, int indexCell,
                                 String str) {
        try {
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setWrapText(true);
            cellStyle.setVerticalAlignment(VerticalAlignment.forInt(1));

            Cell cell = getCell(sheet, indexRow, indexCell);
            cell.setCellStyle(cellStyle);
            if (str.compareTo("") == 0) {
                return;
            }
            if (cell != null) {
                if (str.endsWith("已完成，延后")) {
                    CellStyle cellStyle1 = sheet.getWorkbook()
                            .createCellStyle();

                    cellStyle1.setFillForegroundColor(IndexedColors.YELLOW
                            .getIndex());
                    cell.setCellStyle(cellStyle1);
                }
                if (str.endsWith("已完成，按期")) {
                    CellStyle cellStyle2 = sheet.getWorkbook()
                            .createCellStyle();
                    cellStyle2.setFillPattern(FillPatternType.forInt(1));
                    cellStyle2.setFillForegroundColor(IndexedColors.GREEN
                            .getIndex());
                    cell.setCellStyle(cellStyle2);
                }
                cell.setCellValue(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void FillCell(Sheet sheet, String POS, String str) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            if (str.compareTo("") == 0) {
                return;
            }
            if (cell != null) {
                cell.setCellValue(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void FillCellDate(Sheet sheet, String POS, Date date) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            if (cell != null) {
                cell.setCellValue(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void FillCellint(Sheet sheet, String POS, int num) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            if (cell != null) {
                cell.setCellValue(num);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void FillCellBool(Sheet sheet, String POS, boolean bool) {
        try {
            int Col = getCol(POS);
            int Row = Integer.parseInt(StrCommon.GetNum(POS)) - 1;
            Cell cell = getCell(sheet, Row, Col);
            if (cell != null) {
                cell.setCellValue(bool);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void setCellStyleBorder(Workbook wb, Sheet sheet, int rownum,
                                          int cellnum) {
        CellStyle setBorder = wb.createCellStyle();
        setBorder.setBorderBottom(BorderStyle.NONE);
        setBorder.setBorderLeft(BorderStyle.NONE);
        setBorder.setBorderTop(BorderStyle.NONE);
        setBorder.setBorderRight(BorderStyle.NONE);
        Cell cell = getCell(sheet, rownum, cellnum);
        cell.setCellStyle(setBorder);
    }

    public static void setCellStyleAlign(Workbook wb, Sheet sheet, int rownum,
                                         int cellnum, String align) {
        CellStyle setBorder = wb.createCellStyle();
        if (align.equals("CENTER")) {
            setBorder.setAlignment(HorizontalAlignment.CENTER);
        }
        if (align.equals("LEFT")) {
            setBorder.setAlignment(HorizontalAlignment.LEFT);
        }
        if (align.equals("RIGHT")) {
            setBorder.setAlignment(HorizontalAlignment.RIGHT);
        }
        if (align.equals("VERTICAL_CENTER")) {
            setBorder.setVerticalAlignment(VerticalAlignment.forInt(1));
            setBorder.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        }
        Cell cell = getCell(sheet, rownum, cellnum);
        cell.setCellStyle(setBorder);
    }

    public static void removeMergedRegion(Sheet sheet, int firstRow, int endRow) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress address = sheet.getMergedRegion(i);
            if ((address.getFirstRow() > firstRow)
                    && (address.getFirstRow() < endRow)) {
                sheet.removeMergedRegion(i);
            }
        }
    }

    public static void removeMergedRegion(Sheet sheet, int firstRow,
                                          int endRow, int firstCell, int endCell) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress address = sheet.getMergedRegion(i);
            if ((address.getFirstRow() >= firstRow)
                    && (address.getLastRow() <= endRow)
                    && (address.getFirstColumn() >= firstCell)
                    && (address.getLastColumn() <= endCell)) {
                sheet.removeMergedRegion(i);
                i--;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void copyCell(Sheet sourceSheet, int sourceRowIndex,
                                int sourceColumnIndex, Sheet targetSheet, int targetRowIndex,
                                int targetColumnIndex) {
        Row sourceRow = sourceSheet.getRow(sourceRowIndex);
        if (sourceRow != null) {
            Cell sourceCell = sourceRow.getCell(sourceColumnIndex);
            if (sourceCell != null) {
                Row targetRow = getRow(targetSheet, targetRowIndex);
                Cell targetCell = getCell(targetRow, targetColumnIndex);
                CellStyle cellStyle = sourceCell.getCellStyle();
                if (cellStyle != null) {
                    targetCell.setCellStyle(cellStyle);
                }
                CellType cellType = sourceCell.getCellType();
                targetCell.setCellType(cellType);
                switch (cellType) {
                    case STRING:
                        targetCell.setCellValue(sourceCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case BLANK:
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                    default:
                        System.out.println("Unknow cell type " + cellType);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void copyRows(Sheet sourceSheet, int sourceFirstRow,
                                int sourceLastRow, int columnNum, Sheet targetSheet,
                                int targetFirstRow) {
        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            CellRangeAddress sourceAddress = sourceSheet.getMergedRegion(i);
            if ((sourceAddress.getFirstRow() >= sourceFirstRow)
                    && (sourceAddress.getFirstRow() <= sourceLastRow)) {
                int targetAddressFirstRow = sourceAddress.getFirstRow()
                        - sourceFirstRow + targetFirstRow;
                int targetAddressLastRow = sourceAddress.getLastRow()
                        - sourceFirstRow + targetFirstRow;
                CellRangeAddress targetAddress = new CellRangeAddress(
                        targetAddressFirstRow, targetAddressLastRow,
                        sourceAddress.getFirstColumn(),
                        sourceAddress.getLastColumn());
                targetSheet.addMergedRegion(targetAddress);
            }
        }
        for (int i = sourceFirstRow; i <= sourceLastRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            if (sourceRow != null) {
                short height = sourceRow.getHeight();
                Row targetRow = getRow(targetSheet, targetFirstRow + i
                        - sourceFirstRow);
                targetRow.setHeight(height);
            }
        }
        for (int i = sourceFirstRow; i <= sourceLastRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            if (sourceRow != null) {
                for (int j = 0; j < columnNum; j++) {
                    Cell cell = sourceRow.getCell(j);
                    if (cell != null) {
                        Row targetRow = getRow(targetSheet, targetFirstRow + i
                                - sourceFirstRow);
                        Cell targetCell = getCell(targetRow, j);
                        CellStyle cellstyle = cell.getCellStyle();
                        if (cellstyle != null) {
                            targetCell.setCellStyle(cellstyle);
                        }
                        CellType cellType = cell.getCellType();
                        targetCell.setCellType(cellType);
                        switch (cellType) {
                            case STRING:
                                targetCell.setCellValue(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                targetCell.setCellValue(cell.getNumericCellValue());
                                break;
                            case BLANK:
                                break;
                            case BOOLEAN:
                                targetCell.setCellValue(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                            default:
                                System.out.println("Unknow cell type " + cellType);
                        }
                    }
                }
            }
        }
        for (int i = sourceFirstRow; i <= sourceLastRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = getRow(targetSheet, targetFirstRow + i
                    - sourceFirstRow);
            if (sourceRow != null) {
                for (int j = 0; j < columnNum; j++) {
                    Cell cell = sourceRow.getCell(j);
                    if (cell != null) {
                        Cell targetCell = getCell(targetRow, j);
                        CellType cellType = cell.getCellType();
                        targetCell.setCellType(cellType);
                        switch (cellType) {
                            case STRING:
                                targetCell.setCellValue(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                targetCell.setCellValue(cell.getNumericCellValue());
                                break;
                            case BLANK:
                                break;
                            case BOOLEAN:
                                targetCell.setCellValue(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                            default:
                                System.out.println("Unknow cell type " + cellType);
                        }
                    }
                }
            }
        }
    }

    public static String GetSheetNameFormSheet(Workbook wb, Sheet DestSheet) {
        for (int m = 0; m < wb.getNumberOfSheets(); m++) {
            if (wb.getSheetAt(m) == DestSheet) {
                return wb.getSheetName(m);
            }
        }
        return "";
    }

    public static void DeleteSheets(Workbook wb, int x, int sheetNum) {
        int n = x + 1;
        if (n < sheetNum) {
            try {
                for (; n < sheetNum; n++) {
                    wb.removeSheetAt(x + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void insertRows(Sheet sheet, int startRow, int rows) {
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);
        for (int i = 0; i < rows; i++) {
            Row sourceRow = null;
            Row targetRow = null;
            Cell sourceCell = null;
            Cell targetCell = null;
            sourceRow = sheet.createRow(startRow);
            targetRow = sheet.getRow(startRow + rows);
            sourceRow.setHeight(targetRow.getHeight());
            for (int m = targetRow.getFirstCellNum(); m < targetRow
                    .getPhysicalNumberOfCells(); m++) {
                sourceCell = sourceRow.createCell(m);
                targetCell = targetRow.getCell(m);
                sourceCell.setCellStyle(targetCell.getCellStyle());
                sourceCell.setCellType(targetCell.getCellType());
            }
            startRow++;
        }
    }

    @SuppressWarnings("deprecation")
    public static CellStyle cellStyle142white(Workbook wb, int size,
                                              String isState) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) size);
        if (isState.equals("Bold")) {
            font.setBold(true);
        }
        if (isState.equals("Bold2")) {
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        if (isState.equals("Bold3")) {
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            cellStyle.setWrapText(true);
        }

        if (isState.equals("Red")) {
            font.setBold(true);
            font.setColor(IndexedColors.RED.index);
        }
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (isState.equals("Warp")) {
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        if (isState.equals("Warp2")) {
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
        }
        if (isState.equals("leftop")) {
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        if (isState.equals("leftop2")) {
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        if (size == 14) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        return cellStyle;
    }

    public static CellStyle cellStyleYellow(Workbook wb, String isState) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.RED.index);
        if (isState.equals("Bold")) {
            font.setBold(true);
        }
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    public static void DeleteCellDate(Sheet sheet, int firstRow, int endRow,
                                      int firstCell, int endCell) {
        for (int i = firstRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            for (int j = firstCell; j <= endCell; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellValue("");
            }
        }
    }
}
