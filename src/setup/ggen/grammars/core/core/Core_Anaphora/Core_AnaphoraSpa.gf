--# -path=.:prelude:alltenses

concrete Core_AnaphoraSpa of Core_Anaphora = Core_Spa ** Core_AnaphoraI with (Basic = BasicSpa), (Syntax = SyntaxSpa);