package settings.hometech.com.myapplication;

import java.util.Comparator;

/**
 * Created by zhuyimin on 2017/6/2.
 */

public class ApplicationComparator implements Comparator {

    private ChineseToSpell cn2Spell = null;

    @Override
    public int compare(Object o1, Object o2) {
        cn2Spell = ChineseToSpell.getInstance();
        ApplicationInfo applicationInfoOne = (ApplicationInfo) o1;
        ApplicationInfo applicationInfoTwo = (ApplicationInfo) o2;
        //return applicationInfoOne.appName.compareToIgnoreCase(((ApplicationInfo) o2).appName);
        String str1 = cn2Spell.getSelling(applicationInfoOne.appName);
        String str2 = cn2Spell.getSelling(applicationInfoTwo.appName);
        return str1.compareToIgnoreCase(str2);
    }
}
