package com.devops.common.excel;

import org.jxls.area.Area;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.formula.FormulaProcessor;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: JXLS改进，解决多sheet情况下多余模板sheet
 * @date 2020/8/5 12:43
 */
public class JxlsHelpers extends JxlsHelper {

    public static final String defult = "Template";

    private String templateName = defult;

    public static JxlsHelpers getInstance() {
        return new JxlsHelpers();
    }

    @Override
    public void processTemplate(Context context, Transformer transformer) throws IOException {
        this.getAreaBuilder().setTransformer(transformer);
        List<Area> xlsAreaList = this.getAreaBuilder().build();
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
        }
        if (isProcessFormulas()) {
            for (Area xlsArea : xlsAreaList) {
                setFormulaProcessor(xlsArea);
                xlsArea.processFormulas();
            }
        }
        if (this.isDeleteTemplateSheet()) {
            transformer.deleteSheet(templateName);
        }
        if (isHideTemplateSheet()) {
            transformer.setHidden(templateName, true);
        }
        transformer.write();
    }

    private Area setFormulaProcessor(Area xlsArea) {
        FormulaProcessor fp = this.getFormulaProcessor();
        if (fp == null) {
            if (isUseFastFormulaProcessor()) {
                fp = new FastFormulaProcessor();
            } else {
                fp = new StandardFormulaProcessor();
            }
        }
        xlsArea.setFormulaProcessor(fp);
        return xlsArea;
    }

    public static String getDefult() {
        return defult;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
