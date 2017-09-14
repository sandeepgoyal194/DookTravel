package com.softmine.dooktravel.util;

/**
 * Created by GAURAV on 9/14/2017.
 */

public interface DrawableClickListener {
    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}
