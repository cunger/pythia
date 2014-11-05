--# -path=.:prelude:alltenses

concrete Core_DatatypesSpa of Core_Datatypes = Core_Spa ** Core_DatatypesI 
         with (Symbol = SymbolSpa), 
               (Basic = BasicSpa), 
              (Syntax = SyntaxSpa) ** open ParadigmsSpa in {

    lin 

        Second_name  = { s = "segundo" };
        Minute_name  = { s = "minuto" };
        Hour_name    = { s = "hora" }; 
        Day_name     = { s = "día" }; 
        Week_name    = { s = "semana" }; 
        Month_name   = { s = "mes" }; 
        Year_name    = { s = "año" };

        Second_name_pl = { s = "segundos" };
        Minute_name_pl = { s = "minutos" };
        Hour_name_pl   = { s = "horas" }; 
        Day_name_pl    = { s = "días" }; 
        Week_name_pl   = { s = "semanas" }; 
        Month_name_pl  = { s = "meses" }; 
        Year_name_pl   = { s = "años" };

        Percent_name    = { s = "por ciento" };
        Percent_name_pl = { s = "por ciento" };
        Percent_abbr    = { s = "%" };

    oper

        a_few_Str    : Str = "algunos";
        several_Str  : Str = "algunos";
        at_least_Str : Str = variants { "como mínimo"; "al menos" };
        at_most_Str  : Str = variants { "como máximo"; "todo lo más" };

        slight_A : A = mkA "slight";
        medium_A : A = mkA "moderate";
        high_A   : A = mkA "high";

        slightly_AdA      : AdA = mkAdA "slightly";
        moderately_AdA    : AdA = mkAdA "moderately";
        significantly_AdA : AdA = mkAdA "significantly";
}
