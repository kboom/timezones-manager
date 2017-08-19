package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.Operations.deleteAllFrom

val CLEAN_ALL_TABLES = deleteAllFrom("USER")
