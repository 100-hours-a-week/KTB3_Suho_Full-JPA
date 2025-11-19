// yyyy-mm-ddT00:00:00.00000 -format-> yyyy-mm-dd 00:00:00
export function formatDate(date){
    return date.replace('T', ' ').split('.')[0];
}