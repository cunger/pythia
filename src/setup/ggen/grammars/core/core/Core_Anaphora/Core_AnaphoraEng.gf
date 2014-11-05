--# -path=.:prelude:alltenses

concrete Core_AnaphoraEng of Core_Anaphora = Core_Eng ** Core_AnaphoraI with (Basic = BasicEng), (Syntax = SyntaxEng);