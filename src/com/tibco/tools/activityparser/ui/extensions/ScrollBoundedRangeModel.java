package com.tibco.tools.activityparser.ui.extensions;

import javax.swing.DefaultBoundedRangeModel;

public class ScrollBoundedRangeModel extends DefaultBoundedRangeModel {
int adjValue;
int adjExtent;
int adjMin;
int adjMax;
boolean adjusted;

public void setRangeProperties(int newValue, int newExtent, int
newMin, int newMax, boolean adjusting) {
boolean valueIsAdjusting = getValueIsAdjusting();

if(valueIsAdjusting && adjusting) {
adjValue = newValue;
adjExtent = newExtent;
adjMin = newMin;
adjMax = newMax;
adjusted = true;
return;
}

if(!valueIsAdjusting || adjusting != valueIsAdjusting) {
if(adjusted) {
super.setRangeProperties(adjValue, adjExtent, adjMin,
adjMax, adjusting);
adjusted = false;
}
else {
super.setRangeProperties(newValue, newExtent, newMin,
newMax, adjusting);
}
}
}
}
