--# -path=.:prelude:alltenses

concrete Core_Ger of Core_ = Core_I with 

   (Basic  = BasicGer),
   (Syntax = SyntaxGer), 
   (Extra  = ExtraGer) ** open SyntaxGer, ParadigmsGer in { 

  lin NamedIndividual = mkEntity (mkNP (mkPN "XXX"));

  oper
     XXX_Nn = mkN "XXX" "XXX" neuter;
     XXX_Nf = mkN "XXX" "XXX" feminine;
     XXX_Nm = mkN "XXX" "XXX" masculine;
} 
