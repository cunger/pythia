--# -path=.:prelude:alltenses

concrete Core_DatatypesDut of Core_Datatypes = Core_Dut ** Core_DatatypesI 
         with (Symbol = SymbolDut), 
               (Basic = BasicDut), 
              (Syntax = SyntaxDut) ** open ParadigmsDut in {

    lin 

        Second_name  = { s = "seconde" };
        Minute_name  = { s = "minuut" };
        Hour_name    = { s = "uur" }; 
        Day_name     = { s = "dag" }; 
        Week_name    = { s = "week" }; 
        Month_name   = { s = "maand" }; 
        Year_name    = { s = "jaar" };

        Second_name_pl = { s = "seconden" };
        Minute_name_pl = { s = "minuten" };
        Hour_name_pl   = { s = "uren" }; 
        Day_name_pl    = { s = "dagen" }; 
        Week_name_pl   = { s = "weken" }; 
        Month_name_pl  = { s = "maanden" }; 
        Year_name_pl   = { s = "jaar" };

        Percent_name    = { s = "procent" };
        Percent_name_pl = { s = "procent" };
        Percent_abbr    = { s = "%" };

    oper

        a_few_Str    : Str = "weinig";
        several_Str  : Str = "enkele";
        at_least_Str : Str = "tenminste";
        at_most_Str  : Str = "hogstens";

        slight_A : A = mkA "licht";
        medium_A : A = mkA "moderat";
        high_A   : A = mkA "hoog";

        slightly_AdA      : AdA = lin AdA { s = "licht" };
        moderately_AdA    : AdA = lin AdA { s = "redelijk" };
        significantly_AdA : AdA = lin AdA { s = "flink" };
}
