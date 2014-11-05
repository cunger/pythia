--# -path=.:prelude:alltenses

concrete Core_Spa of Core_ = Core_I with 

   (Basic  = BasicSpa),
   (Syntax = SyntaxSpa), 
   (Extra  = ExtraSpa) ** open SyntaxSpa, ParadigmsSpa in { 

  lin owl_Thing = mkPredicate (mkCN (mkN "cosa" feminine));

}
