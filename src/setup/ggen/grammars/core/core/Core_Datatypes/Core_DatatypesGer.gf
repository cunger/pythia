--# -path=.:prelude:alltenses

concrete Core_DatatypesGer of Core_Datatypes = Core_Ger ** Core_DatatypesI 
         with (Symbol = SymbolGer), 
               (Basic = BasicGer), 
              (Syntax = SyntaxGer) ** open ParadigmsGer in {

    lin 

        Second_name  = { s = "Sekunde" };
        Minute_name  = { s = "Minute" };
        Hour_name    = { s = "Stunde" }; 
        Day_name     = { s = "Tag" }; 
        Week_name    = { s = "Woche" }; 
        Month_name   = { s = "Monat" }; 
        Year_name    = { s = "Jahr" };

        Second_name_pl = { s = "Sekunden" };
        Minute_name_pl = { s = "Minuten" };
        Hour_name_pl   = { s = "Stunden" }; 
        Day_name_pl    = { s = "Tage" }; 
        Week_name_pl   = { s = "Wochen" }; 
        Month_name_pl  = { s = "Monate" }; 
        Year_name_pl   = { s = "Jahre" };

        Meter_name        = { s = "Meter" };
        Meter_name_pl     = { s = "Meter" };
        Meter_abbr        = { s = "m" };
        Kilometer_name    = { s = "Kilometer" };
        Kilometer_name_pl = { s = "Kilometer" };
        Kilometer_abbr    = { s = "km" };

        Percent_name    = { s = "Prozent" };
        Percent_name_pl = { s = "Prozent" };
        Percent_abbr    = { s = "%" };

    oper

        a_few_Str    : Str = "wenige";
        several_Str  : Str = "einige";
        at_least_Str : Str = "mindestens";
        at_most_Str  : Str = "höchstens";

        slight_A : A = mkA "gering";
        medium_A : A = mkA "gut";
        high_A   : A = mkA "hoch";

        slightly_AdA      : AdA = mkAdA "leicht";
        moderately_AdA    : AdA = mkAdA "mäßig";
        significantly_AdA : AdA = mkAdA "erheblich";
}
