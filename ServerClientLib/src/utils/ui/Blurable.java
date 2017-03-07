/**
 * Copyright 2017
 *
 *
 * Sous licence Apache, Version 2.0 (la "Licence");
 * Vous ne pouvez pas utiliser ce fichier sauf en conformité avec la licence.
 * Vous pouvez obtenir une copie de la licence à l'adresse :
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Sauf si requis par la loi en vigueur ou accord écrit, le logiciel
 * Distribué sous licence est distribué «TEL QUEL»,
 * SANS GARANTIES OU CONDITIONS D'AUCUNE SORTE, express ou implicite.
 * Voir la licence pour les autorisations spécifiques aux différentes langues et
 * Limitations sous la licence.
 *
 * Contribué par : Jesus GARNICA OLARRA, Hivinau GRAFFE
 */

package utils.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
@SuppressWarnings("serial")
public class Blurable extends JFrame {

	private static final ConvolveOp CONVOLVE_OP = new ConvolveOp(new Kernel(3, 3, new float[] {
	        .05f, .05f, .05f,
	        .05f, .60f, .05f,
	        .05f, .05f, .05f
	    }), ConvolveOp.EDGE_NO_OP, null);
	
    private transient BufferedImage buf;
	
	public Blurable(String title) {
		super(title);
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		if (isEnabled()) {
			
            super.paint(g);
        } else {
        	
            buf = Optional.ofNullable(buf).filter(bi -> bi.getWidth() == getWidth() && bi.getHeight() == getHeight())
                          .orElseGet(() -> new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB));
            Graphics2D g2 = buf.createGraphics();
            super.paint(g2);
            
            g2.dispose();
            g.drawImage(CONVOLVE_OP.filter(buf, null), 0, 0, null);
        }
	}
}
