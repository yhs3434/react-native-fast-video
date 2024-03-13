#ifdef __cplusplus
#import "react-native-fast-video.h"
#endif

#ifdef RCT_NEW_ARCH_ENABLED
#import "RNFastVideoSpec.h"

@interface FastVideo : NSObject <NativeFastVideoSpec>
#else
#import <React/RCTBridgeModule.h>

@interface FastVideo : NSObject <RCTBridgeModule>
#endif

@end
