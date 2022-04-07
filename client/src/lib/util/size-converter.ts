function bytesToSize(bytes: number, decimals = 2) {
  if (bytes === 0) return '0 Byte'

  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB']
  const k = 1024;

  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return (bytes / Math.pow(k, i)).toFixed(decimals) + ' ' + sizes[i]
}

export {
  bytesToSize,
}
