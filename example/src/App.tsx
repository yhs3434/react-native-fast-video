import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { FastVideoView, multiply } from 'react-native-fast-video';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <FastVideoView
        src={
          'https://file-dev.beaverworksinc.com/images/contents/2024/03/4670643562134223.mp4'
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
