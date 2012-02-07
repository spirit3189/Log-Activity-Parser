
package com.tibco.tools.activityparser.ui.timebar;

import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarRowHeader;
public class TimeBarRowForActivity extends DefaultTimeBarRowModel {
    protected boolean _expanded = false;

    public boolean isExpanded() {
        return _expanded;
    }

    public void setExpanded(boolean expanded) {
        _expanded = expanded;
    }

    public TimeBarRowForActivity(TimeBarRowHeader header) {
        super(header);
    }
}
