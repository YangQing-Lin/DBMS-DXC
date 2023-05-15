<template>
  <div></div>
</template>

<script>
import filesaver from "file-saver";
import * as xlsx from 'xlsx'

export default {
  data() {
    return{

    }
  },
  methods: {
    downinfo(type,filename,id) {

      var wb = xlsx.utils.table_to_book(document.querySelector(id), {
        raw: true,
      });
      var wbout = xlsx.write(wb, {
        bookType: `${type}`,
        bookSST: true,
        type: "array",
      });
      try {
        filesaver.saveAs(
          new Blob([wbout], { type: "application/octet-stream" }),
          `${filename}.${type}`
        );
      } catch (e) {
        if (typeof console !== "undefined") {
          console.log(e, wbout);
        }
      }
      return wbout;

  // const ws = xlsx.utils.json_to_sheet(data, { sheetStubs: true });
  // const wb = xlsx.utils.book_new();
  // xlsx.utils.book_append_sheet(wb, ws, 'Sheet1');
  // xlsx.writeFile(wb, `${filename}.xlsx`);
    },
  },
};
</script>

<style>
</style>