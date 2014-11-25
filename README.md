BufferedInputStream
===================

Enhanced version of BufferedInputStream without available() usage.
 Custom implementation of BuffereInputStream which does not call method "available()" of nested stream at all. 
 It might be reasonable and helpful for those InputStreams which do not impement it or implement with errors [yes, it is truth, there is one very famous IT company G***** that provides API with error inside available(). ].
This implementation uses O(1) RAM consumtion.
