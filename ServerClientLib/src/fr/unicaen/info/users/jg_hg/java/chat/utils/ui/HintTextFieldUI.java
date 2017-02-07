package fr.unicaen.info.users.jg_hg.java.chat.utils.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.*;

public class HintTextFieldUI extends BasicTextFieldUI implements FocusListener {

    private String hint;
    private boolean hideOnFocus;
    private Color color;
    
    public HintTextFieldUI(String hint) {
    	
        this(hint, false);
    }

    public HintTextFieldUI(String hint, boolean hideOnFocus) {
    	
        this(hint, hideOnFocus, null);
    }

    public HintTextFieldUI(String hint, boolean hideOnFocus, Color color) {
    	
        this.hint = hint;
        this.hideOnFocus = hideOnFocus;
        this.color = color;
    }

    public Color getColor() {
    	
        return color;
    }

    public void setColor(Color color) {
    	
        this.color = color;
        repaint();
    }

    private void repaint() {
    	
        if(getComponent() != null) {
        	
            getComponent().repaint();           
        }
    }

    public boolean isHideOnFocus() {
    	
        return hideOnFocus;
    }

    public void setHideOnFocus(boolean hideOnFocus) {
    	
        this.hideOnFocus = hideOnFocus;
        repaint();
    }

    public String getHint() {
    	
        return hint;
    }

    public void setHint(String hint) {
    	
        this.hint = hint;
        repaint();
    }

    @Override
    public void focusGained(FocusEvent e) {
    	
        if(hideOnFocus) repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
    	
        if(hideOnFocus) repaint();
    }

    @Override
    protected void paintSafely(Graphics g) {
        super.paintSafely(g);
        
        JTextComponent comp = getComponent();
        
        if(hint != null && comp.getText().length() == 0 && (!(hideOnFocus && comp.hasFocus()))) {
        	
            if(color != null) {
            	
                g.setColor(color);
            } else {
            	
                g.setColor(comp.getForeground().brighter().brighter().brighter());              
            }
            
            int padding = (comp.getHeight() - comp.getFont().getSize())/2;
            g.drawString(hint, 10, comp.getHeight()-padding-1);          
        }
    }
    
    @Override
    protected void installListeners() {
        super.installListeners();
        
        getComponent().addFocusListener(this);
    }
    
    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        
        getComponent().removeFocusListener(this);
    }
}

