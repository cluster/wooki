//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains utility classes for Date presentation and handling
 * 
 * @author ccordenier
 */
public class DateUtils
{

    public static SimpleDateFormat getDateFormat()
    {
        return new SimpleDateFormat("dd MMMMM yyyy");
    }

    public static SimpleDateFormat getSinceDateFormat()
    {
        return new SimpleDateFormat("MMMMMM dd, yyyy");
    }

    public static SimpleDateFormat getLastModified()
    {
        return new SimpleDateFormat("MMMMMM dd, yyyy hh:mm:ss");
    }

    public static Date oneMonthAgo()
    {
        Calendar c = Calendar.getInstance();
        c.roll(Calendar.MONTH, false);
        return c.getTime();
    }

}
