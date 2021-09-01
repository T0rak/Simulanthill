package ch.hearc.simulanthill.tools;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all assets used in Simulanthill.
 * When a new asset has to be used, it has to be imported here.
 */
public class ColorManagement 
{
    static private float currentHue = 0f;
    static private float currentSat = 1f;
    static private float currentBright = 1f;
    
    static private int currentPow = 0;

    static private int currentIndex = 0;
    static private int pastIndex = 0;

    static private List<Integer> dividerList;
    static private List<Color> colorCacheList;
    /*
    static public Color[] generateTetradicColorSet(Color color)
    {
        float hsv[] = new float[3];
        //System.out.println("RGB: " + color);
        java.awt.Color.RGBtoHSB((int)(color.r * 255f), (int)(color.g * 255f), (int)(color.b * 255f), hsv);
        //System.out.println("HSV: " + hsv[0] + ", " + hsv[1] + ", " + hsv[2]);
        Color colors[] = new Color[4];
        colors[0] = color;

        for (int i = 1; i < 4; i++) 
        {

            hsv[0] = (hsv[0] + 0.25f) % 1f;
            //System.out.println(i + ": " + hsv[0]);
            java.awt.Color newColor = new java.awt.Color(java.awt.Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));

            colors[i] = new Color(newColor.getRed() / 255f, newColor.getGreen() / 255f, newColor.getBlue() / 255f, 1f);
        }

        //System.out.println(colors[0]  + ", " + colors[1] + ", " + colors[2] + ", " + colors[3]);
        return null;
    }
    */

    /**
     * Converts an java.awt.Color hsb format color into a libGdx color
     * @param _h hue [0, 1.0]
     * @param _s saturation [0, 1.0]
     * @param _b brightness [0, 1.0]
     * @return
     */
    private static Color AwtHSBColorToGdxRGBColor(float _h, float _s, float _b) {
        java.awt.Color ret = new java.awt.Color(java.awt.Color.HSBtoRGB(_h, _s, _b));
        return new Color(ret.getRed() / 255f, ret.getGreen() / 255f, ret.getBlue() / 255f, 1f);
    }

    public static Color nextColor() 
    {
        if (colorCacheList != null && currentIndex < colorCacheList.size()) {
            Color ret = colorCacheList.get(currentIndex);
            currentIndex++;
            return ret;
        }

        if (currentIndex == 0) {
            dividerList = new ArrayList<Integer>();
            colorCacheList = new ArrayList<Color>();
            currentIndex++;
            Color ret = AwtHSBColorToGdxRGBColor(currentHue, currentSat, currentBright);
            colorCacheList.add(ret);
            return ret;
        }

        if (currentIndex == 8) {
            currentSat = currentSat - 0.40f;
            currentBright = currentBright - 0.20f;
        }

        if (currentIndex == Math.pow(2, currentPow)) {
            currentPow++;
            dividerList.add((int)Math.pow(2, currentPow));
            pastIndex = 0;
            
        } else {
            dividerList.add(dividerList.get(pastIndex));
            pastIndex++;
        }

        currentHue = currentHue + 1f / dividerList.get(currentIndex - 1);
        currentIndex++;
        Color ret = AwtHSBColorToGdxRGBColor(currentHue, currentSat, currentBright);
        colorCacheList.add(ret);
        return ret;
    }

    public static Color previous() 
    {
        if (currentIndex > 0) {
            currentIndex--;
        }
        return colorCacheList.get(currentIndex);
    }
    
    public static void reset() {
        currentHue = 0f;
        currentSat = 1f;
        currentBright = 1f;
        colorCacheList.clear();
        dividerList.clear();
        currentIndex = 0;
        pastIndex = 0;
        currentPow = 0;
    }
}
