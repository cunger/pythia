--# -path=.:prelude:alltenses

concrete Core_DatatypesEng of Core_Datatypes = Core_Eng ** Core_DatatypesI 
         with (Symbol = SymbolEng), 
               (Basic = BasicEng), 
              (Syntax = SyntaxEng) ** open ParadigmsEng in {

    lin 

        Second_name  = { s = "second" };
        Minute_name  = { s = "minute" };
        Hour_name    = { s = "hour" }; 
        Day_name     = { s = "day" }; 
        Week_name    = { s = "week" }; 
        Month_name   = { s = "month" }; 
        Year_name    = { s = "year" };

        Second_name_pl = { s = "seconds" };
        Minute_name_pl = { s = "minutes" };
        Hour_name_pl   = { s = "hours" }; 
        Day_name_pl    = { s = "days" }; 
        Week_name_pl   = { s = "weeks" }; 
        Month_name_pl  = { s = "months" }; 
        Year_name_pl   = { s = "years" };

        Meter_name        = { s = "meter" };
        Meter_name_pl     = { s = "meters" };
        Meter_abbr        = { s = "m" };
        Kilometer_name    = { s = "kilometer" };
        Kilometer_name_pl = { s = "kilometers" };
        Kilometer_abbr    = { s = "km" };

        Percent_name    = { s = "percent" };
        Percent_name_pl = { s = "percent" };
        Percent_abbr    = { s = "%" };

    oper

        a_few_Str    : Str = "a few";
        several_Str  : Str = "several";
        at_least_Str : Str = "at least";
        at_most_Str  : Str = "at most";

        -- slight_A : A = mkA "slight";
        -- medium_A : A = mkA "moderate";
        -- high_A   : A = mkA "high";

        -- slightly_AdA      : AdA = mkAdA "slightly";
        -- moderately_AdA    : AdA = mkAdA "moderately";
        -- significantly_AdA : AdA = mkAdA "significantly";
}
